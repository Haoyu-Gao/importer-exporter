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

package org.citydb.operation.exporter.database.content;

import org.citydb.operation.exporter.CityGMLExportException;
import org.citydb.operation.exporter.util.GeometrySetter;
import org.citydb.operation.exporter.util.GeometrySetterHandler;

import java.sql.SQLException;

public interface SurfaceGeometryExporter {
    void addBatch(long id, GeometrySetterHandler handler) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.AbstractGeometry setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.Surface setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.CompositeSurface setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.MultiSurface setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.Polygon setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.MultiPolygon setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.Solid setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.CompositeSolid setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.MultiSolid setter) throws CityGMLExportException, SQLException;
    void addBatch(long id, GeometrySetter.Tin setter) throws CityGMLExportException, SQLException;
}