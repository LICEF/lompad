package metatagger;

class OrderedValue {
    Object value;
    int pos;
    boolean isLomVocabulary;

    OrderedValue(Object value, int pos, boolean isLomVocabulary) {
        this.value = value;
        this.pos = pos;
        this.isLomVocabulary = isLomVocabulary;
    }

    public String toString() {
        return (value == null) ? "" :
                (isLomVocabulary ?
                Util.getVocabulary(value.toString()) :
                value.toString());
    }
}