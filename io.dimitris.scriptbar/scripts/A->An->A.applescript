tell application "Skim"
	activate

	if (length of (get selection of document 1)) is 0 then
		return
	end if

	tell document 1
		set selectedText to ((get text for (get selection)) as string)

		if (selectedText is equal to "a") then
			set replacement to "an"
		else if (selectedText is equal to "an") then
			set replacement to "a"
		else
			return
		end if
		set noteText to selectedText & " -> " & replacement
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell

end tell
