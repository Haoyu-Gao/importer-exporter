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
package org.citydb.core.query.filter.selection.operator.comparison;

import org.citydb.core.database.schema.mapping.AbstractProperty;
import org.citydb.core.query.filter.FilterException;
import org.citydb.core.query.filter.selection.expression.Expression;
import org.citydb.core.query.filter.selection.expression.ExpressionName;
import org.citydb.core.query.filter.selection.expression.ValueReference;

public class NullOperator extends AbstractComparisonOperator {
	private Expression operand;
	
	public NullOperator(Expression operand) throws FilterException {
		setOperand(operand);
	}
	
	public boolean isSetOperand() {
		return operand != null;
	}
	
	public Expression getOperand() {
		return operand;
	}

	public void setOperand(Expression operand) throws FilterException {
		if (operand.getExpressionName() == ExpressionName.VALUE_REFERENCE 
				&& !(((ValueReference)operand).getSchemaPath().getLastNode().getPathElement() instanceof AbstractProperty))
			throw new FilterException("The value reference of a null comparison must point to a property.");

		this.operand = operand;
	}
	
	@Override
	public ComparisonOperatorName getOperatorName() {
		return ComparisonOperatorName.NULL;
	}

	@Override
	public NullOperator copy() throws FilterException {
		return new NullOperator(operand);
	}

}