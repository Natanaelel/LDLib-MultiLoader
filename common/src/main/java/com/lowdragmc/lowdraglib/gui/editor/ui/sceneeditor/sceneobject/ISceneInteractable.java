package com.lowdragmc.lowdraglib.gui.editor.ui.sceneeditor.sceneobject;

import com.lowdragmc.lowdraglib.gui.editor.ui.sceneeditor.data.Ray;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;


/**
 * @author KilaBash
 * @date 2024/06/26
 * @implNote A scene object that can be interacted in the scene editor.

 */
@Environment(EnvType.CLIENT)
public interface ISceneInteractable extends ISceneObject {

    /**
     * Get the collision shape of the interactable.
     */
    default VoxelShape getCollisionShape() {
        return Shapes.empty();
    }

    /**
     * @param ray the ray to check collision.
     * @param transform whether to transform the ray to local space.
     * @return the hit result if collide with the ray, null if not collide.
     */
    @Nullable
    default BlockHitResult clip(Ray ray, boolean transform) {
        if (transform) {
            ray = ray.worldToLocal(transform());
        }
        return ray.clip(getCollisionShape());
    }

    default BlockHitResult clip(Ray ray) {
        return clip(ray, true);
    }

    default boolean isCollide(Ray ray) {
        return clip(ray) != null;
    }

    /**
     * Called when the mouse is clicked on the interactable.
     * @return true to consume the event, false to pass it to the next interactable.
     */
    default boolean onMouseClick(Ray mouseRay) {
        return false;
    }

    default void onMouseRelease(Ray mouseRay) {

    }

    default void onMouseDrag(Ray mouseRay) {
    }

}
