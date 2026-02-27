package mvccrud.service;

/**
 * Abstract base class for all service classes.
 *
 * ── OVERRIDING DEMO ──────────────────────────────────────────
 * This class defines a validate() method with a DEFAULT behaviour.
 * Subclasses (e.g. StudentService) can OVERRIDE it to provide
 * their own specific validation logic.
 *
 * Think of it like a contract:
 * "Every service MUST be able to validate input,
 * but HOW it validates is up to each service."
 * ─────────────────────────────────────────────────────────────
 */
public abstract class BaseService {

    // ── To be OVERRIDDEN by each subclass ──
    // Each service defines its own validation rules
    public String validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Input must not be blank."; // generic default rule
        }
        return null; // null = valid
    }

    // ── Cannot be overridden (final) ──
    // Shared utility used by all services — behaviour never changes
    public final String describe() {
        return "Service: " + this.getClass().getSimpleName();
    }
}
