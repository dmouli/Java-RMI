package examples;

import java.util.ArrayList;

import rmi.Remote440Exception;

public class ConcretePersonList implements PersonList {
	public ArrayList<String> list;

	public ConcretePersonList(ArrayList<String> list) {
		this.list = list;
	}

	@Override
	public PersonList addPerson(String person, Boolean allowMultiple)
			throws Remote440Exception {
		ArrayList<String> newList = new ArrayList<String>();
		for (String s : list) {
			newList.add(s);
		}

		if (!newList.contains(person)) {
			newList.add(person);
			return new ConcretePersonList(newList);
		} else {
			if (!allowMultiple) {
				return new ConcretePersonList(newList);
			}
			newList.add(person);
			return new ConcretePersonList(newList);
		}
	}

	@Override
	public PersonList removePerson(String person) throws Remote440Exception {
		if (list.size() == 0) {
			throw new Remote440Exception("List is empty");
		}

		if (list.contains(person)) {
			list.remove(person);
			return this;
		} else {
			throw new Remote440Exception("No such person in list");
		}
	}

	@Override
	public PersonList clearList() throws Remote440Exception {
		ArrayList<String> newList = new ArrayList<String>();
		return (new ConcretePersonList(newList));
	}

	@Override
	public String getList() throws Remote440Exception {
		return list.toString();
	}

	@Override
	public PersonList combineLists(PersonList otherList)
			throws Remote440Exception {
		String otherListStr = otherList.getList();
		otherListStr = otherListStr.replace("[", "");
		otherListStr = otherListStr.replace("]", "");
		otherListStr = otherListStr.replace(",", "");
		String[] others = otherListStr.split(" ");
		for (String other : others) {
			if (other.equals("[") || other.equals(",")) {

			} else {
				list.add(other);
			}
		}
		return this;
	}

}
