package io.github.projectunified.minelib.scheduler.entity;

import com.google.common.cache.LoadingCache;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.BooleanSupplier;

/**
 * A {@link Scheduler} that schedules tasks for an {@link Entity}
 */
public interface EntityScheduler extends Scheduler {
    LoadingCache<Key, EntityScheduler> PROVIDER = Scheduler.createProvider(
            Platform.FOLIA.isPlatform() ? FoliaEntityScheduler::new : BukkitEntityScheduler::new
    );

    /**
     * Get the {@link EntityScheduler} for the given {@link Plugin} and {@link Entity}
     *
     * @param plugin the plugin
     * @param entity the entity
     * @return the scheduler
     */
    static EntityScheduler get(Plugin plugin, Entity entity) {
        try {
            return PROVIDER.get(new Key(plugin, entity));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Run a task
     *
     * @param runnable the runnable
     * @param retired  the runnable called when the entity is retired
     * @return the task
     */
    Task run(Runnable runnable, Runnable retired);

    /**
     * Run a task later
     *
     * @param runnable the runnable
     * @param retired  the runnable called when the entity is retired
     * @param delay    the delay in ticks
     * @return the task
     */
    Task runLater(Runnable runnable, Runnable retired, long delay);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable, returning {@code true} to continue or {@code false} to stop
     * @param retired  the runnable called when the entity is retired
     * @param delay    the delay in ticks
     * @param period   the period in ticks
     * @return the task
     */
    Task runTimer(BooleanSupplier runnable, Runnable retired, long delay, long period);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable
     * @param retired  the runnable called when the entity is retired
     * @param delay    the delay in ticks
     * @param period   the period in ticks
     * @return the task
     */
    default Task runTimer(Runnable runnable, Runnable retired, long delay, long period) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, retired, delay, period);
    }

    @Override
    default Task run(Runnable runnable) {
        return run(runnable, () -> {
        });
    }

    @Override
    default Task runLater(Runnable runnable, long delay) {
        return runLater(runnable, () -> {
        }, delay);
    }

    @Override
    default Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return runTimer(runnable, () -> {
        }, delay, period);
    }

    /**
     * A key for the {@link EntityScheduler}
     */
    class Key {
        public final Plugin plugin;
        public final Entity entity;

        public Key(Plugin plugin, Entity entity) {
            this.plugin = plugin;
            this.entity = entity;
        }

        /**
         * Check if the entity is valid
         *
         * @return true if the entity is valid
         */
        public boolean isEntityValid() {
            if (entity == null) {
                return false;
            }

            if (entity instanceof Player) {
                return ((Player) entity).isOnline();
            }

            return entity.isValid();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(plugin, key.plugin) && Objects.equals(entity, key.entity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(plugin, entity);
        }
    }
}
