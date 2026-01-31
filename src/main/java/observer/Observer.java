package observer;

/**
 * Базовый абстрактный класс для всех наблюдателей.
 * Хранит ссылку на субъект, чтобы наследники могли обращаться к нему в методе {@link #update(Subject)}.
 */
public abstract class Observer implements IObserver {

    /** Субъект, за состоянием которого наблюдает этот объект */
    protected final Subject subject;

    /**
     * Создаёт наблюдателя для заданного субъекта.
     *
     * @param subject субъект (например, TimeServer), на который подписывается наблюдатель
     */
    protected Observer(Subject subject) {
        this.subject = subject;
    }
}
