package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Role;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 05/12/2016.
 */
public class RoleAssignedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final Role role;

    // ******************************
    // Constructors
    // ******************************

    public RoleAssignedEvent(PlayerInstance player, OffsetDateTime timestamp, Role role) {
        super(player, timestamp, EventType.ROLE_ASSIGNED);
        this.role = role;
    }

    // ******************************
    // Getters
    // ******************************

    public Role getRole() {
        return role;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] %s is a %s", getTime(), getPlayer().getName(), role);
    }

}
