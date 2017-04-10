/*
 * Copyright (C) 2017 Florian Dreier
 *
 * This file is part of MyTargets.
 *
 * MyTargets is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
 *
 * MyTargets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package de.dreier.mytargets.features.training.input.opencv.transformation;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PerspectiveTransformation implements ITransformation {

    private final Point center;
    private final Rect outerBounds;

    public PerspectiveTransformation(RotatedRect outerEllipse, Point center) {
        this.center = center;
        outerBounds = ellipseBoundingRect(outerEllipse);
    }

    @Override
    public void transform(Mat src, Mat dst) {
        MatOfPoint2f srcPoints = new MatOfPoint2f(
            new Point(outerBounds.x, outerBounds.y + outerBounds.height * 0.5f),
            new Point(center.x, outerBounds.y),
            new Point(center.x, outerBounds.y + outerBounds.height),
            new Point(
                    outerBounds.x + outerBounds.width,
                    outerBounds.y + outerBounds.height * 0.5f));

        double size = dst.size().width;
        MatOfPoint2f dstPoints = new MatOfPoint2f(
                new Point(0, size / 2.0f),
                new Point(size / 2.0f, 0),
                new Point(size / 2.0, size),
                new Point(size, size / 2.0));

        Mat transform = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
        Imgproc.warpPerspective(src, dst, transform, new Size(size, size));
    }

    // Ported from here: http://code.opencv.org/issues/3396
    private Rect ellipseBoundingRect(RotatedRect ellipse) {
        double degree = ellipse.angle * 3.1415 / 180;
        double majorAxe = ellipse.size.width / 2;
        double minorAxe = ellipse.size.height / 2;
        double x = ellipse.center.x;
        double y = ellipse.center.y;
        double c_degree = Math.cos(degree);
        double s_degree = Math.sin(degree);
        double t1 = Math.atan(-(majorAxe * s_degree) / (minorAxe * c_degree));
        double c_t1 = Math.cos(t1);
        double s_t1 = Math.sin(t1);
        double w1 = majorAxe * c_t1 * c_degree;
        double w2 = minorAxe * s_t1 * s_degree;
        double maxX = x + w1 - w2;
        double minX = x - w1 + w2;

        t1 = Math.atan((minorAxe * c_degree) / (majorAxe * s_degree));
        c_t1 = Math.cos(t1);
        s_t1 = Math.sin(t1);
        w1 = minorAxe * s_t1 * c_degree;
        w2 = majorAxe * c_t1 * s_degree;
        double maxY = y + w1 + w2;
        double minY = y - w1 - w2;
        if (minY > maxY) {
            double temp = minY;
            minY = maxY;
            maxY = temp;
        }
        if (minX > maxX) {
            double temp = minX;
            minX = maxX;
            maxX = temp;
        }
        return new Rect((int) minX, (int) minY, (int) (maxX - minX + 1), (int) (
                maxY - minY + 1));
    }
}