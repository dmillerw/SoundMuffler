package dmillerw.sound.api;

import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author dmillerw
 */
public class APIHandler {

    public static void openConfigurationGUI(EntityPlayer entityPlayer, int x, int y, int z) {
        try {
            Class clazz = Class.forName("dmillerw.sound.core.handler.InternalHandler");
            Method method = clazz.getDeclaredMethod("openConfigurationGUI", EntityPlayer.class, int.class, int.class, int.class);
            method.invoke(clazz, entityPlayer, x, y, z);
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
