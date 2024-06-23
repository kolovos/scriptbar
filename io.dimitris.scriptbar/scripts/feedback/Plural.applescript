tell application "Skim"
	activate

	if (length of (get selection of document 1)) is 0 then
		return
	end if

	tell document 1
		set selectedText to get selection as string

		if (selectedText is equal to "is") then
			set plural to "are"
		else
			set plural to selectedText & "s"
		end if
		set noteText to plural
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell

end tell
