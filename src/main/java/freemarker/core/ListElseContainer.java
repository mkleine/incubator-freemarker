/*
 * Copyright 2014 Attila Szegedi, Daniel Dekany, Jonathan Revusky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package freemarker.core;

import java.io.IOException;

import freemarker.template.TemplateException;

class ListElseContainer extends TemplateElement {

    private final IteratorBlock listPart;
    private final ElseOfList elsePart;

    public ListElseContainer(IteratorBlock listPart, ElseOfList elsePart) {
        setRegulatedChildBufferCapacity(2);
        addRegulatedChild(listPart);
        addRegulatedChild(elsePart);
        this.listPart = listPart;
        this.elsePart = elsePart;
    }

    @Override
    TemplateElementsToVisit accept(Environment env) throws TemplateException, IOException {
        if (!listPart.acceptWithResult(env)) {
            return elsePart.accept(env);
        }
        return null;
    }

    boolean isNestedBlockRepeater() {
        return false;
    }

    protected String dump(boolean canonical) {
        if (canonical) {
            StringBuffer buf = new StringBuffer();
            int ln = getRegulatedChildCount();
            for (int i = 0; i < ln; i++) {
                TemplateElement element = getRegulatedChild(i);
                buf.append(element.dump(canonical));
            }
            buf.append("</#list>");
            return buf.toString();
        } else {
            return getNodeTypeSymbol();
        }
    }

    String getNodeTypeSymbol() {
        return "#list-#else-container";
    }

    int getParameterCount() {
        return 0;
    }

    Object getParameterValue(int idx) {
        throw new IndexOutOfBoundsException();
    }

    ParameterRole getParameterRole(int idx) {
        throw new IndexOutOfBoundsException();
    }

}
