package examples;

import rmi.Remote440;
import rmi.Remote440Exception;

public interface PersonList extends Remote440 {
	public PersonList addPerson(String person, Boolean allowMultiple)
			throws Remote440Exception;

	public PersonList removePerson(String person) throws Remote440Exception;

	public PersonList clearList() throws Remote440Exception;

	public String getList() throws Remote440Exception;

	public PersonList combineLists(PersonList otherList)
			throws Remote440Exception;
}
