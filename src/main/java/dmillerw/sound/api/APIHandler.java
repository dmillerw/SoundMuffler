package dmillerw.sound.api;

import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author dmillerw
 */
public class APIHandler {

    public static void openConfigurationGUI(EntityPlayer entityPlayer) {
        try {
            Class clazz = Class.forName("dmillerw.sound.core.handler.InternalHandler");
            Method method = clazz.getDeclaredMethod("openConfigurationGUI", EntityPlayer.class);
            method.invoke(clazz, entityPlayer);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
