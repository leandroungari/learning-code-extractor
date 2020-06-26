package ast.extractor.mapping;

import com.google.gson.JsonObject;

import persistence.Jsonify;
import ast.extractor.elements.Element;
import ast.extractor.elements.ElementFactory;
import ast.extractor.elements.TempElement;
import ast.snapshot.CommitSnapshot;
import ast.snapshot.Snapshot;

public abstract class Mapping implements Jsonify<Mapping>, Comparable<Mapping> {

	protected Element<?> source;
	protected Element<?> destination;

	protected String oldFile;
	protected String newFile;

	protected CommitSnapshot snapshot;
	protected String commit;

	public Mapping() {

	}

	public void setSnapshot(CommitSnapshot snap) {
		this.snapshot = snap;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	public Boolean isSameType(Mapping other) {

		return getClass() == other.getClass();
	}

	public Element<?> source() {
		if(source instanceof TempElement) {
			var temp = (TempElement) source;
			var snap = Snapshot
				.get(ElementFactory.getTempRepository())
				.commit(commit);
			var result = ElementFactory.create(snap.previous(), temp.path());
			this.source.setType(result.getClass().getSimpleName());
			return result;
		}
		
		return source;
	}

	public Element<?> destination() {
		if(destination instanceof TempElement) {
			var temp = (TempElement) destination;
			var snap = Snapshot
				.get(ElementFactory.getTempRepository())
				.commit(commit);
			var result = ElementFactory.create(snap, temp.path());
			this.destination.setType(result.getClass().getSimpleName());
		}
		return destination;
	}

	public void setOldFile(String oldFile) {
		this.oldFile = oldFile;
	}

	public void setNewFile(String newFile) {
		this.newFile = newFile;
	}

	public String getOldFile() {
		return oldFile;
	}

	public String getNewFile() {
		return newFile;
	}

	@Override
	public abstract String toString();

	public abstract Boolean isParentOf(Mapping other);

	public abstract Boolean isChildOf(Mapping other);

	protected abstract Integer getPriority();

	@Override
	public boolean equals(Object obj) {
		var other = (Mapping) obj;
		return toString().equals(other.toString());
	}

	protected static String pathOfElement(JsonObject object, String element) {

		var item = object.get(element).getAsJsonObject();
		return item.get("path").getAsString();
	}

	@Override
	public int compareTo(Mapping other) {
		if (this.getPriority() < other.getPriority()) {
			return -1;
		} else if (this.getPriority() > other.getPriority()) {
			return 1;
		}
		return 0;
	}

	public static Mapping fromJson(JsonObject mapping, Snapshot snap, String commit) {

		var type = mapping.get("type").getAsString();
		Mapping map = null;
		if ("insert".equals(type)) {
			map = new InsertMapping();
		} else if ("delete".equals(type)) {
			map = new DeleteMapping();
		} else if ("move".equals(type)) {
			map = new MoveMapping();
		} else {
			map = new UpdateMapping();
		}

		if (ElementFactory.isLoadElementEnabled()) {
			var instance = snap.commit(commit);
			map.setSnapshot(instance);
		} else {
			map.setCommit(commit);
		}

		return map.fromJson(mapping);
	}

	protected String prevCommit(String current) {
		var branch = Snapshot
      .get(ElementFactory.getTempRepository())
      .getBranch();

    return branch
      .commit(current)
      .before()
      .id().getName();
	}
}
