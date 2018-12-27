package com.swvl.geometry.shapes;

import com.swvl.geometry.io.TextSerializerHelper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import javax.naming.OperationNotSupportedException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Geometrical point represented by x & y coordinates.
 *
 * @author Hatem Morgan
 */
public class Point extends Shape implements WritableComparable<Point> {
    public final static double EPS = 1e-6; // Epsilon error for comparing floating points

    public double x;
    public double y;

    public Point() {
        this(Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public Point(double x, double y) {
        set(x, y);
    }


    /**
     * A copy constructor from any shape of type Point (or subclass of Point)
     *
     * @param s
     */
    public Point(Point s) {
        this.x = s.x;
        this.y = s.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void write(DataOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    public void readFields(DataInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public double distanceTo(Point p) {
        double dx = p.x - this.x;
        double dy = p.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public Point clone() {
        return new Point(this.x, this.y);
    }

    @Override
    public Point getCenterPoint() {
        return this;
    }

    @Override
    public boolean contains(Shape shape) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Check if point contains a shape is a fetal error");
    }

    @Override
    public boolean isEdgeIntersection(Shape shape) throws OperationNotSupportedException {
        if (shape instanceof Rectangle)
            return shape.isEdgeIntersection(this);

        throw new OperationNotSupportedException("isEdgeIntersection operation in Point is not supported");
    }


    /**
     * Returns the minimal bounding rectangle of this point. This method returns
     * the smallest rectangle that contains this point. For consistency with
     * other methods such as {@link Rectangle#isIntersected(Shape)}, the rectangle
     * cannot have a zero width or height. Thus, we use the method
     * {@link Math#ulp(double)} to compute the smallest non-zero rectangle that
     * contains this point. Thus point = center point of rectangle
     */
    @Override
    public Rectangle getMBR() {
        return new Rectangle(x, y, x, y);
    }

    @Override
    public boolean isIntersected(Shape s) throws OperationNotSupportedException {
        if (s instanceof Point)
            return this.equals(s);

        if (s instanceof Rectangle)
            return s.isIntersected(this);

        if (s instanceof LineSegment)
            return s.isIntersected(this);

        if (s instanceof Polygon)
            return s.isIntersected(this);

        throw new OperationNotSupportedException("isIntersected operation in Point is not supported for " + s.getClass());
    }

    @Override
    public String toString() {
        return "Point: (" + x + "," + y + ")";
    }

    @Override
    public Text toText(Text text) {
        TextSerializerHelper.serializeDouble(x, text, ',');
        TextSerializerHelper.serializeDouble(y, text, '\0');
        return text;
    }

    @Override
    public void fromText(Text text) {
        x = TextSerializerHelper.consumeDouble(text, ',');
        y = TextSerializerHelper.consumeDouble(text, '\0');
    }

    @Override
    public int compareTo(Point pt2) {
        if (this.x - pt2.x < -EPS)
            return -1;

        if (this.x - pt2.x > EPS)
            return 1;

        if (this.y - pt2.y < -EPS)
            return -1;

        if (this.y - pt2.y > EPS)
            return 1;

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point))
            return false;

        Point point = (Point) obj;
        double xDiff = this.x - point.x;
        double yDiff = this.y - point.y;

        return xDiff > -EPS && yDiff > -EPS
                && xDiff < EPS && yDiff < EPS;
    }

    public boolean isGTE(Point point) {
        double xDiff = this.x - point.x;
        double yDiff = this.y - point.y;

        if (xDiff >= EPS) // this.x > point.x
            return true;

        if (xDiff >= -EPS && yDiff >= EPS) // this.x = point.x && this.y > point.y
            return true;

        if (xDiff >= -EPS && yDiff >= -EPS) // this.x = point.x && this.y = point.y
            return true;

        return false;
    }


    public boolean isSTE(Point point) {
        double xDiff = this.x - point.x;
        double yDiff = this.y - point.y;

        if (xDiff <= -EPS) // this.x < point.x
            return true;

        if (xDiff <= EPS && yDiff <= -EPS) // this.x = point.x && this.y < point.y
            return true;

        if (xDiff <= EPS && yDiff <= EPS) // this.x = point.x && this.y = point.y
            return true;

        return false;
    }

    public boolean isGT(Point point) {
        double xDiff = this.x - point.x;
        double yDiff = this.y - point.y;

        if (xDiff >= EPS) // this.x > point.x
            return true;

        if (xDiff >= -EPS && yDiff >= EPS) // this.x = point.x && this.y > point.y
            return true;

        return false;
    }


    public boolean isST(Point point) {
        double xDiff = this.x - point.x;
        double yDiff = this.y - point.y;

        if (xDiff <= -EPS) // this.x < point.x
            return true;

        if (xDiff <= EPS && yDiff <= -EPS) // this.x = point.x && this.y < point.y
            return true;

        return false;
    }
}
