package com.insotheo.fles.interpreter.variable;

public class FlesVariable {
    private final DataType type;
    private final String name;
    private FlesValue value;

    public FlesVariable(DataType type, String name, String value){
        this.type = type;
        this.name = name;
        setValue(value, FlesValue.parseTypeFromDataType(this.type, value));
    }

    public FlesVariable(DataType type, String name){
        this.type = type;
        this.name = name;
        setValue(new FlesValue("", FlesValue.parseTypeFromDataType(this.type, "")));
    }

    public DataType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public FlesValue getValue() {
        return value;
    }

    public void setValue(FlesValue value) {
        this.value = value;
    }

    public void setValue(String value, ValueType valType){
        String resultValue = value;
        if((type == DataType.Int || type == DataType.Float) && FlesValue.inferValueType(value) == valType && valType == ValueType.NumberValue){
            double inputValue = Double.parseDouble(value);
            if(type == DataType.Int){
                int result = ((int)inputValue);
                resultValue = String.valueOf(result);
            }
            else if(type == DataType.Float){
                resultValue = String.valueOf(inputValue);
            }
        }

        if(type == DataType.Char && valType == ValueType.CharValue){
            resultValue = String.valueOf(value.charAt(0));
        }

        this.value = new FlesValue(resultValue, valType);
    }

    public void setData(String data){
        ValueType valType = this.value.getType();
        setValue(data, valType);
    }
}
