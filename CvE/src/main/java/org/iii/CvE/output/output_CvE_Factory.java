package org.iii.CvE.output;

import java.util.ArrayList;

public class output_CvE_Factory
{
	public ArrayList<output_CvE_Interface> get_CvE_output()
	{
		ArrayList<output_CvE_Interface> oci = new ArrayList<output_CvE_Interface>();

		oci.add(new output_CvE_SaveFile());
		oci.add(new output_CvE_ES());
		
		return oci;
	}
}
