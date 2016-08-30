package com.moybl.sidl;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import java.io.InputStream;

public class SimpleIDLResourceLoader extends ResourceLoader {

	public void init(ExtendedProperties extendedProperties) {
	}

	public InputStream getResourceStream(String s) throws ResourceNotFoundException {
		return SimpleIDL.class.getResourceAsStream(s);
	}

	public boolean isSourceModified(Resource resource) {
		return false;
	}

	public long getLastModified(Resource resource) {
		return 0;
	}

}
