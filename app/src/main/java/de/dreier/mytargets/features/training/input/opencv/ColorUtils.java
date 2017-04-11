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

package de.dreier.mytargets.features.training.input.opencv;

import android.graphics.Color;
import android.support.annotation.NonNull;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.dreier.mytargets.shared.models.SelectableZone;
import de.dreier.mytargets.shared.models.Target;
import de.dreier.mytargets.shared.targets.zone.CircularZone;

import static de.dreier.mytargets.shared.utils.Color.BLACK;
import static de.dreier.mytargets.shared.utils.Color.CERULEAN_BLUE;
import static de.dreier.mytargets.shared.utils.Color.FLAMINGO_RED;
import static de.dreier.mytargets.shared.utils.Color.LEMON_YELLOW;
import static de.dreier.mytargets.shared.utils.Color.WHITE;

public class ColorUtils {
    @NonNull
    public static Mat getColorMask(Mat image, int color, boolean noiseRemoval) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);

        Mat mask = new Mat();
        switch (color) {
            case CERULEAN_BLUE:
                Core.inRange(hsv, new Scalar(95, 140, 100), new Scalar(116, 255, 255), mask);
                break;
            case FLAMINGO_RED:
                Core.inRange(hsv, new Scalar(160, 115, 130), new Scalar(180, 255, 255), mask);
                Mat mask2 = new Mat();
                Core.inRange(hsv, new Scalar(0, 115, 130), new Scalar(15, 255, 255), mask2);
                Core.bitwise_or(mask, mask2, mask);
                break;
            case LEMON_YELLOW:
                Core.inRange(hsv, new Scalar(26, 102, 170), new Scalar(30, 255, 255), mask);
                break;
            case BLACK:
                Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(180, 76.5, 100), mask);
                break;
            default:
                float[] hsvColor = new float[3];
                Color.colorToHSV(color, hsvColor);
                float hue = hsvColor[0] / 2f;
                Core.inRange(hsv, new Scalar(hue - 10, 100, 170), new Scalar(
                        hue + 10, 255, 255), mask);
                break;
        }

        if(noiseRemoval) {
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(9, 9));
            Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_DILATE, kernel);
        }

        return mask;
    }

    public static List<CircularZone> getDistinctColorTargetZones(Target target) {
        List<SelectableZone> allZones = target.getSelectableZoneList(0);
        List<CircularZone> distinctZones = new ArrayList<>();
        HashSet<Integer> colors = new HashSet<>();
        colors.add(BLACK);
        colors.add(WHITE);
        for (int i = allZones.size() - 1; i >= 0; i--) {
            SelectableZone zone = allZones.get(i);
            if (!colors.contains(zone.zone.fillColor)) {
                distinctZones.add((CircularZone) zone.zone);
                colors.add(zone.zone.fillColor);
            }
        }
        return distinctZones;
    }
}