package org.archiviststoolkit.importer.MARCXML;

import java.util.List;

import java.util.Vector;

import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.UnsupportedRepeatingDataTypeException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle541Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel) throws UnsupportedRepeatingDataTypeException {
		DataFieldType dataField = (DataFieldType) o;
		String titles[] = {"3","a","b","c","d","e","n","o","f","h"};
		Vector <String> titlesV;
		titlesV = MARCIngest.arrayToVector(titles);
		String noteTitle = "Immediate Source of Acquisiton";
		NotesEtcTypes noteType = NoteEtcTypesUtils.lookupNoteEtcTypeByCannonicalName("Immediate Source of Acquisition note");
		String title = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,titlesV,",");
		ArchDescriptionNotes adn = new ArchDescriptionNotes(resource,noteTitle,NotesEtcTypes.DATA_TYPE_NOTE,resource.getRepeatingData().size()+1,noteType,title);
        adn.setPersistentId(resource.getNextPersistentIdAndIncrement());
        resource.addRepeatingData(adn);
	}
    public List getChildren(Object element)
    {
        return null;
    }
}