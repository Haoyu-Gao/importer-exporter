/*
 * 3D City Database - The Open Source CityGML Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2013 - 2021
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.lrg.tum.de/gis/
 *
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 *
 * Virtual City Systems, Berlin <https://vc.systems/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citydb.cli.options.vis;

import org.citydb.config.project.kmlExporter.KmlTiling;
import org.citydb.config.project.kmlExporter.KmlTilingMode;
import org.citydb.config.project.kmlExporter.KmlTilingOptions;
import org.citydb.plugin.cli.CliOption;
import picocli.CommandLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TilingOption implements CliOption {
    @CommandLine.Option(names = {"-g", "--tiling"}, paramLabel = "<rows,columns | auto[=length]>",
            description = "Tile the bounding box into a rows x columns grid or use 'auto' to create tiles with a " +
                    "fixed side length. Optionally specify the side length in meters (default: 125).")
    private String tiling;

    private KmlTiling kmlTiling;

    public KmlTiling toKmlTiling() {
        return kmlTiling;
    }

    @Override
    public void preprocess(CommandLine commandLine) throws Exception {
        kmlTiling = new KmlTiling();

        if (tiling != null) {
            Pattern pattern = Pattern.compile("^(.+,.+)|(auto(?:=.+)?)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(tiling);
            if (matcher.matches()) {
                if (matcher.group(2) != null) {
                    kmlTiling.setMode(KmlTilingMode.AUTOMATIC);
                    String[] tokens = tiling.split("=");
                    if (tokens.length == 2) {
                        try {
                            double length = Double.parseDouble(tokens[1]);
                            if (length <= 0) {
                                throw new NumberFormatException();
                            }

                            KmlTilingOptions tilingOptions = new KmlTilingOptions();
                            tilingOptions.setAutoTileSideLength(length);
                            kmlTiling.setTilingOptions(tilingOptions);
                        } catch (NumberFormatException e) {
                            throw new CommandLine.ParameterException(commandLine,
                                    "Error: The side length for automatic tiling must be a positive number " +
                                            "but was '" + tokens[1] + "'");
                        }
                    }
                } else {
                    kmlTiling.setMode(KmlTilingMode.MANUAL);
                    String[] numbers = tiling.split(",");
                    try {
                        int rows = Integer.parseInt(numbers[0]);
                        int columns = Integer.parseInt(numbers[1]);
                        if (rows <= 0 || columns <= 0) {
                            throw new NumberFormatException();
                        }

                        kmlTiling.setRows(rows);
                        kmlTiling.setColumns(columns);
                    } catch (NumberFormatException e) {
                        throw new CommandLine.ParameterException(commandLine,
                                "Error: The number of rows and columns for tiling must be positive integers but were '" +
                                        String.join(",", numbers) + "'");
                    }
                }
            } else {
                throw new CommandLine.ParameterException(commandLine,
                        "Error: The value for '--bbox-tiling' must be in ROWS,COLUMNS or AUTO[=LENGTH] format " +
                                "but was '" + tiling + "'");
            }
        } else {
            kmlTiling.setMode(KmlTilingMode.NO_TILING);
        }
    }
}