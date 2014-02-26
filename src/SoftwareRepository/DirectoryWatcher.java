package SoftwareRepository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DirectoryWatcher extends Thread implements IDirectoryPublisher {
	private List<IDirectoryObserver> IObservers;
	private String filename;
	private String action;
	private String path;

	public DirectoryWatcher(String path) {
		IObservers = new ArrayList<IDirectoryObserver>();
		this.path=path;
	}

	public void registerObserver(IDirectoryObserver observer) {
		IObservers.add(observer);
	}

	public void removeObserver(IDirectoryObserver observer) {
		if (!IObservers.isEmpty())
			IObservers.remove(observer);
	}

	public void notifyObserver() {
		if (!IObservers.isEmpty()) {
			for (IDirectoryObserver observer : IObservers) {
				observer.update(action, filename);
			}
		}
	}

	public void run() {
		WatchService watcher = null;

		try { 
			watcher = FileSystems.getDefault().newWatchService(); //neuen WatchService erzeugen
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try { //Registriere WatchService auf einem Pfad mit gewuenschten Events
			Paths.get(path).register(watcher,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) { //dauerschleife zum �berwachen
			WatchKey key = null;
			try {
				key = watcher.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

			for (WatchEvent<?> event : key.pollEvents()){
				action=""+event.kind();
				filename=""+event.context();
				notifyObserver(); //Observer informieren
			}
			
			key.reset();
		}
	}

}
