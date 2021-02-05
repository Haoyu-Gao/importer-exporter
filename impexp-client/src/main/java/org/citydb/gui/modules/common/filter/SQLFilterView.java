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

package org.citydb.gui.modules.common.filter;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.citydb.config.gui.components.SQLExportFilterComponent;
import org.citydb.config.i18n.Language;
import org.citydb.config.project.exporter.SimpleQuery;
import org.citydb.config.project.query.filter.selection.sql.SelectOperator;
import org.citydb.gui.factory.PopupMenuDecorator;
import org.citydb.gui.factory.RSyntaxTextAreaHelper;
import org.citydb.gui.util.GuiUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class SQLFilterView extends FilterView {
    private JPanel component;
    private RSyntaxTextArea sqlText;
    private RTextScrollPane scrollPane;
    private JButton addButton;
    private JButton removeButton;

    private int additionalRows;
    private int rowHeight;

    private final Supplier<SQLExportFilterComponent> sqlFilterComponentSupplier;

    public SQLFilterView(Supplier<SimpleQuery> simpleQuerySupplier, Supplier<SQLExportFilterComponent> sqlFilterComponentSupplier) {
        super(simpleQuerySupplier);
        this.sqlFilterComponentSupplier = sqlFilterComponentSupplier;
        init();
    }

    private void init() {
        component = new JPanel();
        component.setLayout(new GridBagLayout());

        addButton = new JButton(new FlatSVGIcon("org/citydb/gui/icons/add.svg"));
        removeButton = new JButton(new FlatSVGIcon("org/citydb/gui/icons/remove.svg"));

        sqlText = new RSyntaxTextArea("", 5, 1);
        sqlText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        sqlText.setAutoIndentEnabled(true);
        sqlText.setHighlightCurrentLine(true);
        sqlText.setTabSize(2);
        rowHeight = sqlText.getFont().getSize() + 5;
        scrollPane = new RTextScrollPane(sqlText);

        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder());
        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.setFloatable(false);
        toolBar.setOrientation(JToolBar.VERTICAL);

        component.add(scrollPane, GuiUtil.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0));
        component.add(toolBar, GuiUtil.setConstraints(1, 0, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 5, 0, 0));

        addButton.addActionListener(e -> {
            Dimension size = scrollPane.getPreferredSize();
            size.height += rowHeight;
            additionalRows++;

            scrollPane.setPreferredSize(size);
            component.revalidate();
            component.repaint();

            if (!removeButton.isEnabled())
                removeButton.setEnabled(true);
        });

        removeButton.addActionListener(e -> {
            if (additionalRows > 0) {
                Dimension size = scrollPane.getPreferredSize();
                size.height -= rowHeight;
                additionalRows--;

                scrollPane.setPreferredSize(size);
                component.revalidate();
                component.repaint();

                if (additionalRows == 0)
                    removeButton.setEnabled(false);
            }
        });

        RSyntaxTextAreaHelper.installDefaultTheme(sqlText);
        PopupMenuDecorator.getInstance().decorate(sqlText);
    }

    @Override
    public void doTranslation() {
        addButton.setToolTipText(Language.I18N.getString("filter.label.sql.increase"));
        removeButton.setToolTipText(Language.I18N.getString("filter.label.sql.decrease"));
    }

    @Override
    public void setEnabled(boolean enable) {
        scrollPane.getHorizontalScrollBar().setEnabled(enable);
        scrollPane.getVerticalScrollBar().setEnabled(enable);
        scrollPane.getViewport().getView().setEnabled(enable);

        addButton.setEnabled(enable);
        removeButton.setEnabled(enable && additionalRows > 0);
    }

    @Override
    public String getLocalizedTitle() {
        return Language.I18N.getString("filter.border.sql");
    }

    @Override
    public Component getViewComponent() {
        return component;
    }

    @Override
    public String getToolTip() {
        return null;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void loadSettings() {
        SimpleQuery query = simpleQuerySupplier.get();

        SelectOperator sql = query.getSQLFilter();
        sqlText.setText(sql.getValue());

        additionalRows = sqlFilterComponentSupplier.get().getAdditionalRows();
        SwingUtilities.invokeLater(() -> {
            if (additionalRows > 0) {
                Dimension size = scrollPane.getPreferredSize();
                size.height += additionalRows * rowHeight;

                scrollPane.setPreferredSize(size);
                component.revalidate();
                component.repaint();
            } else
                additionalRows = 0;

            removeButton.setEnabled(additionalRows > 0);
        });
    }

    @Override
    public void setSettings() {
        SimpleQuery query = simpleQuerySupplier.get();

        SelectOperator sql = query.getSQLFilter();
        sql.reset();
        String value = sqlText.getText().trim();
        if (!value.isEmpty())
            sql.setValue(value.replaceAll(";", " "));

        sqlFilterComponentSupplier.get().setAdditionalRows(additionalRows);
    }
}