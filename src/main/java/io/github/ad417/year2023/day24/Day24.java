package io.github.ad417.year2023.day24;

import tk.vivas.adventofcode.AocUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 {
    // First problem to effectively need doubles. Neat.
    record Hail(long x, long y, long z, long dx, long dy, long dz) {
        static final double MIN = 2e14;
        static final double MAX = 4e14;
        static Hail makeHail(String line) {
            Matcher m = Pattern.compile(
                    "(-?\\d+), +(-?\\d+), +(-?\\d+) @ +(-?\\d+), +(-?\\d+), +(-?\\d+)"
            ).matcher(line);
            if (!m.find()) throw new IllegalArgumentException("Invalid Hail: " + line);
            return new Hail(
                    Long.parseLong(m.group(1)),
                    Long.parseLong(m.group(2)),
                    Long.parseLong(m.group(3)),
                    Long.parseLong(m.group(4)),
                    Long.parseLong(m.group(5)),
                    Long.parseLong(m.group(6))
            );
        }
        public boolean intersects2D(Hail other) {
            // Convert the first equation to y = mx+b.
            double b1 = this.dy * ((double) -this.x / this.dx) + this.y;
            double m1 = (double) this.dy / this.dx;
            double b2 = other.dy * ((double) -other.x / other.dx) + other.y;
            double m2 = (double) other.dy / other.dx;

            //System.out.println("y = "+m1+"x + "+b1);
            //System.out.println("y = "+m2+"x + "+b2);

            // If m1 and m2 are equal, I'm just going to assume they are
            // parallel and thus never intersect.
            if (m2 == m1) return false;

            // This gives 2 equations and two unknowns: the X and Y positions
            // of the intersection, wherever and whenever it may be.
            double xInt = (b1 - b2) / (m2 - m1);
            double yInt = m1 * xInt + b1;

            //System.out.println("INTERSECT: ("+xInt+", "+yInt+")");

            // Ensure this is within the critical region!
            if (MIN >= xInt || xInt >= MAX) return false;
            if (MIN >= yInt || yInt >= MAX) return false;

            // Check what times these occur.
            // They must both be in the future.
            double t1 = (xInt - this.x) / this.dx;
            double t2 = (xInt - other.x) / other.dx;
            return (t1 >= 0 && t2 >= 0);
        }

        private Hail makeOriginFor(Hail other) {
            return new Hail(
                    other.x - this.x, other.y - this.y, other.z - this.z,
                    other.dx - this.dx, other.dy - this.dy, other.dz - this.dz
            );
        }

        public Hail step(long time) {
            return new Hail(x + dx * time, y + dy * time, z + dz * time, dx, dy, dz);
        }
        public Vector3 pos() {
            return new Vector3(x, y, z);
        }
        public Vector3 vel() {
            return new Vector3(dx, dy, dz);
        }
    }

    record Vector3(long x, long y, long z) {
        public Vector3 add(Vector3 other) {
            return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
        }
        public Vector3 sub(Vector3 other) {
            return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
        }
        public Vector3 times(long scalar) {
            return new Vector3(x * scalar, y * scalar, z * scalar);
        }
        public Vector3 div(long scalar) {
            return new Vector3(x / scalar, y / scalar, z / scalar);
        }
        public Vector3 cross(Vector3 other) {
            return new Vector3(
                    this.y * other.z - this.z * other.y,
                    this.z * other.x - this.x * other.z,
                    this.x * other.y - this.y * other.x
            );
        }

        public long dot(Vector3 other) {
            return this.x * other.x + this.y * other.y + this.z * other.z;
        }

        private static long GCF(long a, long b) {
            if (b == 0) return a;
            return GCF(b, a % b);
        }

        public Vector3 reduce() {
            long g = GCF(GCF(x, y), z);
            return new Vector3(x/g, y/g, z/g);
        }
    }

    private static int partA(List<Hail> hail) {
        int sum = 0;
        for (int i = 0; i < hail.size(); i++) {
            Hail h1 = hail.get(i);
            for (int k = i+1; k < hail.size(); k++) {
                Hail h2 = hail.get(k);
                if (h1.intersects2D(h2)) sum++;
            }
        }
        return sum;
    }

    private static long partB(List<Hail> hail) {
        // Step 1: Pick 4 "arbitrary" hailstones.
        List<Hail> used = hail.subList(0, 4);
        // Step 2: Change the reference frame of the problem to make one of
        // them be the origin at all times.
        List<Hail> adjusted = used.stream().map(x -> used.get(0).makeOriginFor(x)).toList();

        // Step 3: Determine the plane formed by the reference hail and any
        // other hailstone. The plane's formula is equivalent to the following:
        //      ax + by + cz + d = 0
        // It turns out the cross-product of ant two vectors that lie on the
        // plane will compute a, b, and c for free.
        // Since I'm lazy and not knowledgeable enough to know how to do the
        // math from just a line, I'll just take one of the hailstones'
        // positions at t=0 and t=1, and use the displacement vector from the
        // origin as the vectors for the cross-product.
        // The value of d is 0, because (0,0,0) is a point on the plane.
        Vector3 first = adjusted.get(1).pos();
        Vector3 second = adjusted.get(1).step(1).pos();

        Vector3 plane = first.cross(second).reduce();

        // Step 4: The other hailstone will intersect the plane at some point.
        // The rock must hit the hail at the instant it intersects the plane.
        // Thanks to the following:
        // https://en.wikipedia.org/wiki/Line%E2%80%93plane_intersection#Parametric_form
        // We also get the time to intercept for free.
        // This is important for later.

        Hail intersecting = adjusted.get(2);
        long t1 = (
                plane.dot(intersecting.pos()) /
                new Vector3(0,0,0).sub(intersecting.vel()).dot(plane)
        );
        // TODO: determine how to address potential integer overflow

        // Step 4b: Do it again, with another hailstone.
        // There is definitely a more intelligent method, by determining where
        // and when the line formed by the first stone and the coplanar line
        // intersect, but I'm lazy.
        intersecting = adjusted.get(3);
        long t2 = (
                plane.dot(intersecting.pos()) /
                        new Vector3(0,0,0).sub(intersecting.vel()).dot(plane)
        );

        // Step 5: This gives us 2 points, which are collinear with the origin.
        // So, just determine how far the rock moves between t1 and t2 in the
        // three directions, and then dp/dt = v.
        Vector3 intersect1 = adjusted.get(2).step(t1).pos();
        Vector3 intersect2 = adjusted.get(3).step(t2).pos();
        long dt = t1 - t2;

        Vector3 velocity = intersect1.sub(intersect2).div(dt);
        // Step 5b: Then, use p = p0 + vt to determine the starting position.
        Vector3 position = intersect1.sub(velocity.times(t1));

         // Step 6: Convert the rock back to the original reference frame.
        Vector3 absolutePosition = position.add(hail.get(0).pos());
        Vector3 absoluteVelocity = velocity.add(hail.get(0).vel());

        System.out.println("Rock = " + absoluteVelocity + "t * " + absolutePosition);

        return absolutePosition.x + absolutePosition.y + absolutePosition.z;
    }

    public static void main(String[] args) {
        String data = AocUtils.readPuzzleInput().trim();
        /*data = """
                19, 13, 30 @ -2,  1, -2
                18, 19, 22 @ -1, -1, -2
                20, 25, 34 @ -2, -2, -4
                12, 31, 28 @ -1, -2, -1
                20, 19, 15 @  1, -5, -3""";*/
        List<Hail> hail = data.lines().map(Hail::makeHail).toList();

        //hail.forEach(System.out::println);
        System.out.println(partA(hail));
        System.out.println(partB(hail));

    }

}
