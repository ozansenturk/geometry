package com.swvl.geometry.shapes;

import com.swvl.geometry.Utilities;
import org.apache.hadoop.io.Text;

import javax.naming.OperationNotSupportedException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LineSegment extends Shape {
    public Point startPoint; // Start point of line segment
    public Point endPoint; // End point of line segment

    /* LineSegment equation params ax + by + c = 0*/
    public double a;
    public double b;
    public double c;

    public LineSegment() {

    }

    public void init(Point p1, Point p2) {
        this.startPoint = p1;
        this.endPoint = p2;

        if (Math.abs(p1.x - p2.x) < Point.EPS) { // vertical line
            this.a = 1;
            this.b = 0;
            this.c = -p1.x;
        } else {
            this.a = -((endPoint.y - startPoint.y) / (endPoint.x - startPoint.x));
            this.b = 1;  // fix value of b to 1
            this.c = -(a * startPoint.x) - startPoint.y;
        }
    }

    public LineSegment(Point a, Point b) {
        init(a, b);
    }

    @Override
    public Rectangle getMBR() {
        return new Rectangle(startPoint, endPoint);
    }

    @Override
    public double distanceTo(Point p) {
        /* transform line ap to vector*/
        double apx = p.x - startPoint.x;
        double apy = p.y - startPoint.y;

        /* transform line ab to vector*/
        double abx = endPoint.x - startPoint.x;
        double aby = endPoint.y - startPoint.y;

        /* Calculate unit vector ab^ of vector ab */
        double vectorMagnitude = Math.sqrt(abx * abx + aby * aby);
        double ux = abx / vectorMagnitude;
        double uy = aby / vectorMagnitude;

        /*
         * Get the scalar projection b of vector ap on ab.
         * s = ||ap|| * cosθ =  dotProduct(ap, ab^) / ||ab^||
         * We use the unit vector of ab because magnitude of unit vector = 1
         */
        double u = apx * ux + apy * uy;

        /* use pythagoras to calculate perpendicular distance between ap and ab*/
        if (u < 0.0) // closer to a
            return p.distanceTo(startPoint); // Euclidean distance between p and a


        if (u > 1.0)  // closer to b
            return p.distanceTo(endPoint); // Euclidean distance between p and b

        /*
         * Translate point a by a scaled magnitude u of vector ab
         * (multiplying ab^ by u to get scaled vector in ab direction)
         */
        double cx = startPoint.x + (ux * u);
        double cy = startPoint.y + (uy * u);
        return p.distanceTo(new Point(cx, cy));
    }

    @Override
    public boolean isIntersected(Shape shape) throws OperationNotSupportedException {
        if (shape instanceof Point)
            return Utilities.lineSegmentPointIntersection((Point) shape, this);

        if (shape instanceof Rectangle)
            return shape.isIntersected(this);

        if (shape instanceof LineSegment)
            return isLineSegmentIntersection((LineSegment) shape);

        if (shape instanceof Polygon)
            return shape.isIntersected(this);

        throw new OperationNotSupportedException("isIntersected operation in LineSegment does not support " + shape.getClass());
    }


    private boolean isLineSegmentIntersection(LineSegment line) throws OperationNotSupportedException {

        /* Check for general case that line segments intersect at boundaries*/
        if (this.startPoint.equals(line.startPoint)
                || this.startPoint.equals(line.endPoint)
                || this.endPoint.equals(line.startPoint)
                || this.endPoint.equals(line.endPoint))
            return true;

        /* solve simultaneous equation of two 2 line equation with two unknowns (x,y) */
        Point p = new Point();
        p.x = (line.b * this.c - this.b * line.c) / (line.a * this.b - this.a * line.b);

        /* check vertical line (b=0) to avoid dividing by zero */
        if (this.b < Point.EPS && this.b > -Point.EPS)
            p.y = -(line.a * p.x) - line.c; // invoker is a vertical line so calculate y from line
        else
            p.y = -(this.a * p.x) - this.c;

        /* Check that intersection point is on both lines */
        return this.isIntersected(p) && line.isIntersected(p);
    }

    @Override
    public Shape clone() {
        return new LineSegment(new Point(startPoint.x, startPoint.y),
                new Point(endPoint.x, endPoint.y));
    }

    @Override
    public Point getCenterPoint() {
        return new Point(
                (startPoint.x + endPoint.x) / 2,
                (startPoint.y + endPoint.y) / 2);
    }

    @Override
    public boolean contains(Shape shape) throws OperationNotSupportedException {
        if (shape instanceof Point)
            return isIntersected((Point) shape);


        if (shape instanceof LineSegment) {
            /* Check that both points of line exist on invoker line */
            LineSegment line = (LineSegment) shape;
            return this.isIntersected(line.startPoint)
                    && this.isIntersected(line.endPoint);
        }

        if (shape instanceof Rectangle)
            throw new OperationNotSupportedException("Check if LineSegment contains" +
                    " a Rectangle is a fatal error");

        if (shape instanceof Polygon)
            throw new OperationNotSupportedException("Check if LineSegment contains" +
                    " a Polygon is a fatal error");

        throw new OperationNotSupportedException("Contains operation in LineSegment does not support " + shape.getClass());
    }

    @Override
    public boolean isEdgeIntersection(Shape shape) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("isEdgeIntersection is not supported for line");
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        startPoint.write(dataOutput);
        endPoint.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        startPoint = new Point();
        startPoint.readFields(dataInput);

        endPoint = new Point();
        endPoint.readFields(dataInput);

        init(startPoint, endPoint);
    }

    @Override
    public Text toText(Text text) {
        startPoint.toText(text);
        endPoint.toText(text);
        return text;
    }

    @Override
    public void fromText(Text text) {
        startPoint = new Point();
        startPoint.fromText(text);

        endPoint = new Point();
        endPoint.fromText(text);

        init(startPoint, endPoint);
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LineSegment))
            return false;

        LineSegment line = (LineSegment) obj;

        return this.startPoint.equals(line.startPoint)
                && this.endPoint.equals(line.endPoint);
    }
}
