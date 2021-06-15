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
package org.citydb.gui.operation.preferences.preferences;

import org.citydb.config.Config;
import org.citydb.config.i18n.Language;
import org.citydb.config.project.global.GlobalConfig;
import org.citydb.config.project.global.LanguageType;
import org.citydb.gui.ImpExpGui;
import org.citydb.gui.components.TitledPanel;
import org.citydb.gui.operation.common.AbstractPreferencesComponent;
import org.citydb.gui.util.GuiUtil;

import javax.swing.*;
import java.awt.*;

public class LanguagePanel extends AbstractPreferencesComponent {
	private final ImpExpGui mainView;
	private JRadioButton importLanguageRadioDe;
	private JRadioButton importLanguageRadioEn;
	private TitledPanel language;

	public LanguagePanel(ImpExpGui mainView, Config config) {
		super(config);
		this.mainView = mainView;
		initGui();
	}
	
	@Override
	public boolean isModified() {
		LanguageType language = config.getGlobalConfig().getLanguage();
		
		if (importLanguageRadioDe.isSelected() && !(language == LanguageType.DE)) return true;
		if (importLanguageRadioEn.isSelected() && !(language == LanguageType.EN)) return true;
		return false;
	}
	
	private void initGui() {		
		importLanguageRadioDe = new JRadioButton();
		importLanguageRadioEn = new JRadioButton();
		ButtonGroup importLanguageRadio = new ButtonGroup();
		importLanguageRadio.add(importLanguageRadioDe);
		importLanguageRadio.add(importLanguageRadioEn);
		
		setLayout(new GridBagLayout());
		{
			JPanel content = new JPanel();
			content.setLayout(new GridBagLayout());
			{
				content.add(importLanguageRadioDe, GuiUtil.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0));
				content.add(importLanguageRadioEn, GuiUtil.setConstraints(0, 1, 1, 1, GridBagConstraints.BOTH, 5, 0, 0, 0));
			}

			language = new TitledPanel().build(content);
		}

		add(language, GuiUtil.setConstraints(0, 0, 1, 0, GridBagConstraints.BOTH, 0, 0, 0, 0));
	}
	
	@Override
	public void doTranslation() {
		language.setTitle(Language.I18N.getString("pref.general.language.border.selection"));
		importLanguageRadioDe.setText(Language.I18N.getString("pref.general.language.label.de"));
		importLanguageRadioEn.setText(Language.I18N.getString("pref.general.language.label.en"));
	}
	
	@Override
	public void loadSettings() {		
		LanguageType language = config.getGlobalConfig().getLanguage();
		
		if (language == LanguageType.DE) {
			importLanguageRadioDe.setSelected(true);
		} else if (language == LanguageType.EN) {
			importLanguageRadioEn.setSelected(true);
		}
	}
	
	@Override
	public void setSettings() {
		GlobalConfig globalConfig = config.getGlobalConfig();
		
		if (importLanguageRadioDe.isSelected()) {
			globalConfig.setLanguage(LanguageType.DE);
		} else if (importLanguageRadioEn.isSelected()) {
			globalConfig.setLanguage(LanguageType.EN);
		}
		
		mainView.doTranslation();
	}
	
	@Override
	public String getTitle() {
		return Language.I18N.getString("pref.tree.general.language");
	}
}