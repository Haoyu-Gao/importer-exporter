/*
 * 3D City Database - The Open Source CityGML Database
 * http://www.3dcitydb.org/
 *
 * Copyright 2013 - 2020
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 *
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 *
 * virtualcitySYSTEMS GmbH, Berlin <http://www.virtualcitysystems.de/>
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

import org.citydb.config.project.kmlExporter.SimpleKmlQuery;
import org.citydb.config.project.query.filter.selection.id.ResourceIdOperator;
import org.citydb.plugin.cli.CliOption;
import org.citydb.plugin.cli.ResourceIdOption;
import org.citydb.plugin.cli.TypeNamesOption;
import picocli.CommandLine;

public class QueryOption implements CliOption {
    @CommandLine.ArgGroup(exclusive = false)
    private TypeNamesOption typeNamesOption;

    @CommandLine.ArgGroup
    private ResourceIdOption resourceIdOption;

    @CommandLine.ArgGroup
    private BoundingBoxOption boundingBoxOption;

    @CommandLine.ArgGroup
    private TilingOption tilingOption;

    public SimpleKmlQuery toSimpleKmlQuery() {
        SimpleKmlQuery query = new SimpleKmlQuery();

        if (typeNamesOption != null) {
            query.setUseTypeNames(true);
            query.setFeatureTypeFilter(typeNamesOption.toFeatureTypeFilter());
        }

        if (resourceIdOption != null) {
            ResourceIdOperator idOperator = resourceIdOption.toResourceIdOperator();
            if (idOperator != null) {
                query.setUseGmlIdFilter(true);
                query.setGmlIdFilter(idOperator);
            }
        }

        if (tilingOption != null) {
            query.setSpatialFilter(tilingOption.toKmlTiling());
        }

        if (boundingBoxOption != null) {
            query.setUseBboxFilter(true);
            query.getSpatialFilter().setExtent(boundingBoxOption.toBoundingBox());
        }

        return query;
    }

    @Override
    public void preprocess(CommandLine commandLine) throws Exception {
        if (typeNamesOption != null) {
            typeNamesOption.preprocess(commandLine);
        }

        if (boundingBoxOption != null) {
            boundingBoxOption.preprocess(commandLine);
        }

        if (tilingOption != null) {
            tilingOption.preprocess(commandLine);
        }
    }
}