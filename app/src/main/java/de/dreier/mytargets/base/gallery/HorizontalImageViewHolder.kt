/*
 * Copyright (C) 2018 Florian Dreier
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

package de.dreier.mytargets.base.gallery

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView

import de.dreier.mytargets.R

class HorizontalImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var camera: ImageView = itemView.findViewById(R.id.camera)
    var image: ImageView = itemView.findViewById(R.id.iv)

}
