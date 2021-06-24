package org.citydb.gui.operation.preferences.plugin;

import org.citydb.config.Config;
import org.citydb.gui.operation.common.DefaultPreferencesEntry;

public class PluginsOverviewEntry extends DefaultPreferencesEntry {

    public PluginsOverviewEntry(PluginsOverviewPlugin plugin, Config config) {
        super(new PluginsOverviewPanel(plugin, config));
    }
}
