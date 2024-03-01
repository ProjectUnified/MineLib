package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.function.BooleanSupplier;

public interface EntityScheduler extends Scheduler {
    ObjectProvider<Key, EntityScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaEntityScheduler::new),
            ObjectProvider.entry(BukkitEntityScheduler::new)
    );

    static EntityScheduler get(Plugin plugin, Entity entity) {
        return PROVIDER.get(new Key(plugin, entity));
    }

    Task run(Runnable runnable, Runnable retired);

    Task runLater(Runnable runnable, Runnable retired, TaskTime delay);

    Task runTimer(BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime);

    default Task runTimer(Runnable runnable, Runnable retired, TimerTaskTime timerTaskTime) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, retired, timerTaskTime);
    }

    default Task runLaterWithFinalizer(Runnable runnable, Runnable finalizer, TaskTime delay) {
        return runLater(() -> {
            try {
                runnable.run();
            } finally {
                finalizer.run();
            }
        }, finalizer, delay);
    }

    @Override
    default Task run(Runnable runnable) {
        return run(runnable, () -> {
        });
    }

    @Override
    default Task runLater(Runnable runnable, TaskTime delay) {
        return runLater(runnable, () -> {
        }, delay);
    }

    @Override
    default Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return runTimer(runnable, () -> {
        }, timerTaskTime);
    }

    class Key {
        public final Plugin plugin;
        public final Entity entity;

        public Key(Plugin plugin, Entity entity) {
            this.plugin = plugin;
            this.entity = entity;
        }

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
