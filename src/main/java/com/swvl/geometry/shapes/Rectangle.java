package com.swvl.geometry.shapes;

import com.swvl.geometry.Utilities;

import javax.naming.OperationNotSupportedException;


/**
 * Implementation of a rectangle.
 *
 * @author Hatem Morgan
 */
public class Rectangle implements Shape, Comparable<Rectangle> {
    public Point minPoint; // point with minimum coordinates
    public Point maxPoint; // point with maximum coordinates

    public void validate() {
        if (minPoint == null || maxPoint == null)
            throw new IllegalArgumentException("Bottom-left and Upper-right points" +
                    "are not initialized.");
    }

    public Rectangle() {
        this(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public Rectangle(Point minPoint, Point maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public void set(Rectangle rect) {
        this.minPoint = rect.minPoint.clone();
        this.maxPoint = rect.maxPoint.clone();
    }

    public Rectangle(double x1, double y1, double x2, double y2) {
        this.minPoint = new Point(x1, y1);
        this.maxPoint = new Point(x2, y2);
    }

    @Override
    public int compareTo(Rectangle rectangle) {
        validate();


        // Sort by minPoint's x then y
        if (this.minPoint.x - rectangle.minPoint.x < -EPS)
            return -1;
        if (this.minPoint.x - rectangle.minPoint.x > EPS)
            return 1;
        if (this.minPoint.y - rectangle.minPoint.y < -EPS)
            return -1;
        if (this.minPoint.y - rectangle.minPoint.y > EPS)
            return 1;

        // Sort by maxPoint's x then y
        if (this.maxPoint.x - rectangle.maxPoint.x < -EPS)
            return -1;
        if (this.maxPoint.x - rectangle.maxPoint.x > EPS)
            return 1;
        if (this.maxPoint.y - rectangle.maxPoint.y < -EPS)
            return -1;
        if (this.maxPoint.y - rectangle.maxPoint.y > EPS)
            return 1;

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        validate();

        if (!(obj instanceof Rectangle))
            return false;

        Rectangle r2 = (Rectangle) obj;
        return this.minPoint.equals(r2.minPoint) && this.maxPoint.equals(r2.maxPoint);
    }

    @Override
    public int hashCode() {
        validate();

        int result;
        long temp;
        temp = Double.doubleToLongBits(this.minPoint.x);
        result = (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minPoint.y);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxPoint.x);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxPoint.y);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    /**
     * Get the distance between point and the center of rectangle
     *
     * @param p the point
     * @return
     */
    @Override
    public double distanceTo(Point p) throws OperationNotSupportedException {
        validate();

        if (containsPoints(p)) // point inside rectangle
            return 0;

        Point p1 = new Point(this.maxPoint.x, this.minPoint.y); // bottom-right point
        Point p2 = new Point(this.minPoint.x, this.maxPoint.y); // upper-left point

        /* Edges of a rectangle */
        LineSegment[] rectEdges = new LineSegment[]{
                new LineSegment(this.minPoint, p1),
                new LineSegment(p1, this.maxPoint),
                new LineSegment(this.maxPoint, p2),
                new LineSegment(p2, this.minPoint)
        };

        double distance = Double.MAX_VALUE;

        for (LineSegment edge : rectEdges)
            distance = Math.min(distance, edge.distanceTo(p));

        return distance;
    }

    @Override
    public Rectangle clone() {
        validate();

        return new Rectangle(minPoint.x, minPoint.y, maxPoint.x, maxPoint.y);
    }

    @Override
    public Rectangle getMBR() {
        validate();

        return new Rectangle(minPoint.x, minPoint.y, maxPoint.x, maxPoint.y);
    }


    @Override
    public boolean isIntersected(Shape shape) throws OperationNotSupportedException {
        validate();

        if (shape instanceof Point)
            return Utilities.rectanglePointIntersection((Point) shape, this);


        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;

            /* part of one rectangle is inside the other one */
            return this.maxPoint.x + EPS > rect.minPoint.x // this.maxPoint.x >= rect.minPoint.x
                    && this.maxPoint.y + EPS > rect.minPoint.y // this.maxPoint.y >= rect.minPoint.y
                    && rect.maxPoint.x + EPS > this.minPoint.x // this.minPoint.x <= rect.maxPoint.x
                    && rect.maxPoint.y + EPS > this.minPoint.y; // this.minPoint.y <= rect.maxPoint.y
        }

        if (shape instanceof LineSegment)
            return Utilities.rectangelLineSegementIntersection((LineSegment) shape, this);

        if (shape instanceof Polygon)
            return Utilities.polygonRectangleIntersection(this, (Polygon) shape);

        throw new OperationNotSupportedException("Contains operation in Rectangle does not support " + shape.getClass());
    }


    /**
     * Check for intersection between any of edges of this shape with any of edges of given shape
     *
     * @param shape The other shape to test for edge intersection with this shape
     * @return <code>true</code> if this shape has edge(s) that intersects with edge(s) of given shape; <code>false</code> otherwise.
     */
    public boolean isEdgeIntersection(Shape shape) throws OperationNotSupportedException {
        validate();

        if (shape instanceof Point)
            return isIntersected(shape);

        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.isIntersected(this.maxPoint)
                    || this.isIntersected(rect.minPoint)
                    || rect.isIntersected(this.minPoint)
                    || this.isIntersected(rect.maxPoint);
        }

        throw new OperationNotSupportedException("isEdgeIntersection operation in Rectangle does not support " + shape.getClass());

    }

    @Override
    public boolean contains(Shape shape) throws OperationNotSupportedException {
        validate();

        if (shape instanceof Point)
            return Utilities.rectanglePointIntersection((Point) shape, this);

        if (shape instanceof Rectangle)
            return containsPoints(((Rectangle) shape).minPoint,
                    ((Rectangle) shape).maxPoint);


        /* For a rectangle to contain a line segment,
         * the two points of line segment should be inside the rectangle
         */
        if (shape instanceof LineSegment)
            return containsPoints(((LineSegment) shape).p1, ((LineSegment) shape).p2);

        if (shape instanceof Polygon)
            return containsPoints(((Polygon) shape).points);

        throw new OperationNotSupportedException("Contains operation in Rectangle does not support " + shape.getClass());
    }

    /**
     * Iterate over points and check if that all points are inside the rectangle
     */
    private boolean containsPoints(Point... points) throws OperationNotSupportedException {
        for (Point point : points)
            if (!this.isIntersected(point))
                return false;

        return true;
    }

    /**
     * Open bounded rectangle is a rectangle which in inclusive at left and bottom edges and exclusive
     * at upper and right edges. Thus, Points on right or upper borders are exclusive
     *
     * @param p
     * @return
     */
    public boolean upperOpenBoundedContains(Point p) {
        validate();

        double minDiffX = this.minPoint.x - p.x;
        double minDiffY = this.minPoint.y - p.y;
        double maxDiffX = this.maxPoint.x - p.x;
        double maxDiffY = this.maxPoint.y - p.y;

        if (minDiffX >= EPS) // to the left of rect.minPoint
            return false;

        if (minDiffY >= EPS) // to the bottom of rect.minPoint
            return false;

        if (maxDiffX <= -EPS) // to the right of rect.maxPoint
            return false;

        if (maxDiffY <= -EPS) // above of rect.maxPoint
            return false;

        /* p is either to the right or top or equal of rect.minPoint */

        if (maxDiffX >= EPS) // to the right of rect.minPoint and to the left of rect.maxPoint
            if (maxDiffY >= EPS) // to the top of rect.minPoint and to the bottom of rect.maxPoint
                return true;

        /* borders(edges) intersection */

        // the point equal to min Point
        if (this.minPoint.equals(p))
            return true;

        /* bottom border (edge): to the right of rect.minPoint with the same y-coordinate and to the left of rect.maxPoint */
        if (minDiffY < EPS && minDiffY > -EPS)  // y-coordinates of minPoint and Point are equal
            return minDiffX <= -EPS && maxDiffX >= EPS; // to right of minPoint and to the left of maxPoint

        /* left border (edge): above rect.minPoint with the same x-coordinate and below of rect.maxPoint */
        if (minDiffX < EPS && minDiffX > -EPS)  // x-coordinates of minPoint and point are equal
            return minDiffY <= -EPS && maxDiffY >= EPS; // above minPoint and below of maxPoint

        return false;
    }


    public void expand(Point point) {
        validate();

        if (point.x < this.minPoint.x)
            this.minPoint.x = point.x;
        if (point.y < this.minPoint.y)
            this.minPoint.y = point.y;
        if (point.x > this.maxPoint.x)
            this.maxPoint.x = point.x;
        if (point.y > this.maxPoint.y)
            this.maxPoint.y = point.y;
    }

    public void expand(final Shape s) {
        validate();

        Rectangle r = s.getMBR();
        if (r.minPoint.x < this.minPoint.x)
            this.minPoint.x = r.minPoint.x;

        if (r.maxPoint.x > this.maxPoint.x)
            this.maxPoint.x = r.maxPoint.x;

        if (r.minPoint.y < this.minPoint.y)
            this.minPoint.y = r.minPoint.y;

        if (r.maxPoint.y > this.maxPoint.y)
            this.maxPoint.y = r.maxPoint.y;
    }


    @Override
    public Point getCenterPoint() {
        validate();

        return new Point((this.minPoint.x + this.maxPoint.x) / 2, (this.minPoint.y + this.maxPoint.y) / 2);
    }

    @Override
    public String toString() {
        validate();

        return "Rectangle: (" + minPoint.x + "," + minPoint.y + ")-(" + maxPoint.x + "," + maxPoint.y + ")";
    }


    public double getHeight() {
        validate();

        return maxPoint.y - minPoint.y;
    }

    public double getWidth() {
        validate();

        return maxPoint.x - minPoint.x;
    }
}
