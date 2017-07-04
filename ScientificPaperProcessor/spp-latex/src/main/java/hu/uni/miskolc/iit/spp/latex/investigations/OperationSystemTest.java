package hu.uni.miskolc.iit.spp.latex.investigations;

import java.util.Collection;

import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class OperationSystemTest {

	private String osName;
	private Collection<String> possibleOperationSystem;
	
	public OperationSystemTest(String osName, Collection<String> possibleOperationSystem) {
		this.osName = osName;
		this.possibleOperationSystem = possibleOperationSystem;
	}
	
	public String whichMatch() throws NotSupportedOperationSystemException {
		for(String name : OperationSystemTest.getInArray(this.possibleOperationSystem)) {
			if(this.osName.toLowerCase().contains(name) == true) {
				return name;
			}
		}
		throw new NotSupportedOperationSystemException("Could not compile because the operation system (" + this.osName + ") not suppoerted.\n"
													+ "Supoorted operation systems: " + this.possibleOperationSystem.toString());
	}
	
	private static String[] getInArray(Collection<String> possibleOperationSystem) {
		String[] possibleOperationSystemInArray = possibleOperationSystem.toArray(new String[possibleOperationSystem.size()]);
		return possibleOperationSystemInArray;
	}
}