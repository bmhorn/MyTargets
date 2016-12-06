/*
 * Copyright (C) 2016 Florian Dreier
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

package de.dreier.mytargets.utils.rules;

import java.util.Random;

import de.dreier.mytargets.managers.SettingsManager;
import de.dreier.mytargets.shared.models.Bow;
import de.dreier.mytargets.shared.models.Dimension;
import de.dreier.mytargets.shared.models.Round;
import de.dreier.mytargets.shared.models.StandardRound;
import de.dreier.mytargets.shared.models.Target;
import de.dreier.mytargets.shared.models.Training;
import de.dreier.mytargets.shared.targets.models.WAFull;
import de.dreier.mytargets.shared.views.TargetViewBase;

public class MiniDbTestRule extends DbTestRuleBase {

    @Override
    protected void addDatabaseContent() {
        SettingsManager.setTarget(
                new Target(WAFull.ID, 0, new Dimension(122, Dimension.Unit.CENTIMETER)));
        SettingsManager.setDistance(new Dimension(50, Dimension.Unit.METER));
        SettingsManager.setIndoor(false);
        SettingsManager.setInputMethod(TargetViewBase.EInputMethod.PLOTTING);
        SettingsManager.setTimerEnabled(true);
        SettingsManager.setArrowsPerEnd(6);
        Bow bow = addBow();
        addRandomTraining(578459341);
        addRandomTraining(454459456);
    }

    private void addRandomTraining(int seed) {
        Random generator = new Random(seed);
        StandardRound standardRound = standardRoundDataSource.get(32);

        Training training = insertDefaultTraining(standardRound, generator);

        Round round1 = new Round();
        round1.trainingId = training.getId();
        round1.info = standardRound.rounds.get(0);
        round1.info.target = round1.info.targetTemplate;
        round1.comment = "";
        roundDataSource.update(round1);

        Round round2 = new Round();
        round2.trainingId = training.getId();
        round2.info = standardRound.rounds.get(1);
        round2.info.target = round2.info.targetTemplate;
        round2.comment = "";
        roundDataSource.update(round2);

        passeDataSource.update(randomPasse(training, round1, 6, generator));
        passeDataSource.update(randomPasse(training, round1, 6, generator));

        passeDataSource.update(randomPasse(training, round2, 6, generator));
        passeDataSource.update(randomPasse(training, round2, 6, generator));
    }

}