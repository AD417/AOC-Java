package io.github.ad417.util;

import java.util.HashSet;
import java.util.Set;

public record Coordinate(int row, int col) {
    /**
     * Add two 2D Coordinates together.
     * @param other another Coordinate to add to this one.
     * @return a new Coordinate containing the sum of these positions.
     */
    public Coordinate add(Coordinate other) {
        return new Coordinate(this.row + other.row, this.col + other.col);
    }

    /**
     * Add a 2D offset to this coordinate.
     * @param row the number of rows to add to this coordinate.
     * @param col the number of columns to add to this coordinate.
     * @return a new Coordinate with the specified offset.
     */
    public Coordinate add(int row, int col) {
        return new Coordinate(this.row + row, this.col + col);
    }

    /**
     * Multiply / "scale" this Coordinate by a scalar value. Intended to be
     * applied to a unit Coordinate to get some larger value.
     * @param scalar the amount to scale the Coordinate by.
     * @return a Coordinate with its row and column amounts scaled by the
     * give amount.
     */
    public Coordinate mult(int scalar) {
        return new Coordinate(row*scalar, col*scalar);
    }

    /**
     * Get the four positions orthogonally adjacent to this current position.
     * That is, the positions directly North, South, East, and West of here.
     * @return a Set of Coordinates that can be iterated over.
     */
    public Set<Coordinate> orthogonal() {
        Set<Coordinate> orth = new HashSet<>();
        for (int row = this.row-1; row <= this.row+1; row++) {
            for (int col = this.col-1; col <= this.col+1; col++) {
                if ((row != this.row) == (col != this.col)) continue;
                orth.add(new Coordinate(row, col));
            }
        }
        return orth;
    }

    /**
     * Get the eight positions adjacent to this current position.
     * @return a Set of Coordinates that can be iterated over.
     */
    public Set<Coordinate> adjacent() {
        Set<Coordinate> adj = new HashSet<>();
        for (int row = this.row-1; row <= this.row+1; row++) {
            for (int col = this.col-1; col <= this.col+1; col++) {
                adj.add(new Coordinate(row, col));
            }
        }
        return adj;
    }

    /**
     * If this coordinate represents a position on a finite 2D plane, determine
     * if the coordinate is a valid position on the plane. Assumes that the
     * smallest valid position is (0,0).
     * @param maxRow the largest possible row value, exclusive.
     * @param maxCol the largest possible column value, exclusive.
     * @return true iff this coordinate is within those bounds.
     */
    public boolean inBounds(int maxRow, int maxCol) {
        return 0 <= row && row < maxRow && 0 <= col && col < maxCol;
    }

    /**
     * Determine the Manhatten (taxicab) distance between two points.
     * @param other another Coordinate to determine the distance to.
     * @return the sum of the absolute values of the components.
     */
    public int distanceTo(Coordinate other) {
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }

    public Coordinate wrap(Coordinate largest) {
        int row = this.row % largest.row();
        if (row < 0) row += largest.row();
        int col = this.col % largest.col();
        if (col < 0) col += largest.col();
        return new Coordinate(row, col);
    }

    @Override
    public String toString() {
        return "(" +row+", "+col+")";
    }
}
