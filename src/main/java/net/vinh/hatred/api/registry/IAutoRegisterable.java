package net.vinh.hatred.api.registry;

/**
 * No method body is required for the {@link IAutoRegisterable#staticInit()} method.
 * <p> This requires the subclass to be referenced in {@code fabric.mod.json} through the {@code hatred:registry} key.</p>
 */
public interface IAutoRegisterable {
    void staticInit();
}
