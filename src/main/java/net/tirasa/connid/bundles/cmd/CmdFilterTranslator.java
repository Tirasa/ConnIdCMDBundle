/**
 * Copyright (C) 2011 ConnId (connid-dev@googlegroups.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tirasa.connid.bundles.cmd;

import net.tirasa.connid.bundles.cmd.search.Operand;
import net.tirasa.connid.bundles.cmd.search.Operator;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsIgnoreCaseFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.StringFilter;

public class CmdFilterTranslator extends AbstractFilterTranslator<Operand>{
    @Override
    protected Operand createEqualsIgnoreCaseExpression(final EqualsIgnoreCaseFilter filter, final boolean not) {
        return createOperand(Operator.EQ, filter, not);
    }

    @Override
    protected Operand createStartsWithExpression(final StartsWithFilter filter, final boolean not) {
        return createOperand(Operator.SW, filter, not);
    }

    @Override
    protected Operand createEndsWithExpression(final EndsWithFilter filter, final boolean not) {
        return createOperand(Operator.EW, filter, not);
    }

    @Override
    protected Operand createContainsExpression(final ContainsFilter filter, final boolean not) {
        return createOperand(Operator.C, filter, not);
    }
    
    private Operand createOperand(final Operator op, final StringFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }
        String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }
        switch (op) {
            case EQ:
                return new Operand(Operator.EQ, filter.getAttribute().getName(), value, not);
            case SW:
                return new Operand(Operator.SW, filter.getAttribute().getName(),
                        value.substring(0, value.length() - 1), not);
            case EW:
                return new Operand(Operator.EW, filter.getAttribute().getName(), value.substring(1), not);
            case C:
                return new Operand(Operator.C, filter.getAttribute().getName(),
                        value.substring(1, value.length() - 1), not);
            default:
                return null;
        }
    }

}
