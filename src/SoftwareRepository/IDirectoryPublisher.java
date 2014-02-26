package SoftwareRepository;

public interface IDirectoryPublisher {
    public void registerObserver(IDirectoryObserver ob);
    public void removeObserver(IDirectoryObserver ob);
    public void notifyObserver();
}