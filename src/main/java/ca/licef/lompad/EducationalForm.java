/*
 * Copyright (C) 2005  Alexis Miara (alexis.miara@licef.ca)
 *
 * This file is part of LomPad.
 *
 * LomPad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LomPad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LomPad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.licef.lompad;

class EducationalForm extends FormContainer {
    public EducationalForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm interactType = new VocabularyForm("5.1", true, false, null, true);
        interactType.addToggle();
        interactType.setAlignRight();
        interactType.addFormContent();
        addComponent(interactType);

        VocabularyForm learnResType = new VocabularyForm("5.2", true, true, new Object[]{null}, true);
        learnResType.addToggle();
        learnResType.addFormContent();
        addComponent(learnResType);

        VocabularyForm interactLevel = new VocabularyForm("5.3", true, false, null, true);
        interactLevel.addToggle();
        interactLevel.setAlignRight();
        interactLevel.addFormContent();
        addComponent(interactLevel);

        VocabularyForm semant = new VocabularyForm("5.4", true, false, null, true);
        semant.addToggle();
        semant.setAlignRight();
        semant.addFormContent();
        addComponent(semant);

        VocabularyForm intended = new VocabularyForm("5.5", true, true, new Object[]{null}, true);
        intended.addToggle();
        intended.addFormContent();
        addComponent(intended);

        VocabularyForm context = new VocabularyForm("5.6", true, true, new Object[]{null}, true);
        context.addToggle();
        context.addFormContent();
        addComponent(context);

        MultiLangstringForm typicalAge = new MultiLangstringForm("5.7", true, true, true, "", true);
        typicalAge.addToggle();
        typicalAge.addFormContent();
        addComponent(typicalAge);

        VocabularyForm difficulty = new VocabularyForm("5.8", true, false, null, true);
        difficulty.addToggle();
        difficulty.setAlignRight();
        difficulty.addFormContent();
        addComponent(difficulty);

        DurationForm typicalLearn = new DurationForm("5.9", true, false);
        typicalLearn.addToggle();
        typicalLearn.addFormContent();
        addComponent(typicalLearn);

        MultiLangstringForm description = new MultiLangstringForm("5.10", true, true, false, "", true);
        description.addToggle();
        description.addFormContent();
        addComponent(description);

        VocabularyForm language = new VocabularyForm("5.11", true, true,
                LangstringForm.values, new Object[]{null}, false);
        language.setEditable(true);
        language.addToggle();
        language.addFormContent();
        addComponent(language);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
