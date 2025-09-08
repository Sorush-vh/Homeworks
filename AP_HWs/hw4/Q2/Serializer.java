import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import static java.lang.reflect.Modifier.isStatic;

public class Serializer {

    private Object object;

    public String getSerializedString() {
        try {
            return createJsonForObject(object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object=object;
    }

    private String createJsonForObject(Object object) throws IllegalArgumentException, IllegalAccessException{
       
        Field[] fields = addParentClassFields(object);
        String[] fieldnames=new String[fields.length];
        HashMap<String,Field> fieldMapper=new HashMap<>();

        for (int i = 0; i < fields.length; i++){ 
            fieldnames[i]=fields[i].getName();
            fieldMapper.put(fieldnames[i],fields[i]);
        }    
        Arrays.sort(fieldnames);
        for (int i = 0; i < fieldnames.length; i++) 
            fields[i]=fieldMapper.get(fieldnames[i]);
        

        String output="";
        int i=0;
        for (Field field : fields) {
            if (isStatic(field.getModifiers())) 
                continue;
            output=output.concat(encodeSingleField(field, object.getClass(), object));
            if(i!=fields.length-1)
                output=output.concat(",");
            i++;
        }
        return output;
    }

    private Field[] addParentClassFields(Object object){
        Field[] self=object.getClass().getDeclaredFields();
        Field[] parentFields=object.getClass().getSuperclass().getDeclaredFields();
        Field[] result=new Field[self.length+parentFields.length];

        for (int i = 0; i < self.length; i++) 
            result[i]=self[i];

            int j=0;
        for (int i = self.length; i < result.length; i++) {
            result[i]=parentFields[j];
            j++;
        }
            return result;
    }

    private String encodeSingleField(Field field,Class klass,Object object) throws IllegalArgumentException, IllegalAccessException{

        StringBuilder stringBuilder = new StringBuilder();
        if(!Collection.class.isAssignableFrom(field.getType()))
        field.setAccessible(true);

        if(field.getAnnotation(Rename.class) == null )
            stringBuilder.append(field.getName() + ":");
        else{
            Rename rename = field.getAnnotation(Rename.class);
            if(rename.name()!= null && rename.name().length()>0)
                stringBuilder.append(rename.name() + ":");
            else stringBuilder.append(field.getName() + ":");
        }



        String value="";
        if (field.getType().isPrimitive() && !field.getType().equals(Character.TYPE)) 
            value=field.get(object).toString();
        
        else if (field.getType().equals(String.class) || field.getType().isPrimitive()) 
            value=field.get(object).toString();

            stringBuilder.append(value);
        
            return stringBuilder.toString();
        }

}
