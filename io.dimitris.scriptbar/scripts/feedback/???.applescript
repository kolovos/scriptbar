# Pops up a dialog to enter a comment on the selection
tell application "Skim"
	activate
	
	if (length of (get selection of document 1)) is 0 then
		return
	end if
	
	tell document 1
		set noteText to "???"
		make note with properties {type:highlight note, selection:selection, text:noteText, color:cyan}
	end tell
	
end tell
