/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package com.bc.ceres.compiler;

import java.util.HashMap;


public class Product {
    HashMap<String,Band> bandMap;

    public Product(Band[] bands) {
        bandMap  = new HashMap<String, Band>();
        for (int i = 0; i < bands.length; i++) {
            Band band = bands[i];
            bandMap.put(band.getName(), band);
        }
    }

    public Band getBand(String name) {
        return bandMap.get(name);
    }
}
