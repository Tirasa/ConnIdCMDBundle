/* 
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2011 ConnId. All rights reserved.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License.
 * 
 * You can obtain a copy of the License at
 * http://opensource.org/licenses/cddl1.php
 * See the License for the specific language governing permissions and limitations
 * under the License.
 * 
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://opensource.org/licenses/cddl1.php.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package net.tirasa.connid.bundles.cmd.search;

import org.identityconnectors.framework.common.objects.Uid;

public class Operand {

    private Operator operator = null;

    private String attributeName = "";

    private String attributeValue = "";

    private boolean not = false;

    private Operand firstOperand = null;

    private Operand secondOperand = null;

    public Operand(final Operator operator, final String name, final String value, final boolean not) {
        this.operator = operator;
        attributeName = name;
        attributeValue = value;
        this.not = not;
    }

    public Operand(final Operator operator, final Operand firstOperand, final Operand secondOperand) {
        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }   

    public final Operand getFirstOperand() {
        return firstOperand;
    }

    public final Operand getSecondOperand() {
        return secondOperand;
    }

    public final String getAttributeName() {
        return attributeName;
    }

    public final boolean isUid() {
        return attributeName.equalsIgnoreCase(Uid.NAME);
    }

    public final String getAttributeValue() {
        return attributeValue;
    }

    public final boolean isNot() {
        return not;
    }

    public final Operator getOperator() {
        return operator;
    }
}
