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
package org.citydb.core.operation.importer.concurrent;

import org.citydb.config.Config;
import org.citydb.util.concurrent.Worker;
import org.citydb.util.concurrent.WorkerFactory;
import org.citydb.util.concurrent.WorkerPool;
import org.citydb.util.event.EventDispatcher;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.xml.io.reader.XMLChunk;

public class FeatureReaderWorkerFactory implements WorkerFactory<XMLChunk> {
    private final WorkerPool<CityGML> workerPool;
    private final Config config;
    private final EventDispatcher eventDispatcher;

    public FeatureReaderWorkerFactory(WorkerPool<CityGML> workerPool,
                                      Config config,
                                      EventDispatcher eventDispatcher) {
        this.workerPool = workerPool;
        this.config = config;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Worker<XMLChunk> createWorker() {
        return new FeatureReaderWorker(workerPool, config, eventDispatcher);
    }
}
