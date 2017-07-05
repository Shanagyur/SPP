package hu.uni.miskolc.iit.spp.latex.investigations;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class OperationSystemTest {

	private String osName;
	private static SupportedOperationSystems supportedOperationSystems;
	
	public OperationSystemTest(String osName) {
		this.osName = osName;
	}
	
	public String whichMatch() throws NotSupportedOperationSystemException {
		for(SupportedOperationSystems name : OperationSystemTest.supportedOperationSystems.values()) {
			if(this.osName.toLowerCase().contains(name.getStringValue()) == true) {
				return name.getStringValue();
			}
		}
		throw new NotSupportedOperationSystemException("Operation system not supported");
	}
}