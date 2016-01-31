tell application "Skim"
	activate
	set xml to "<document name=" & quote & name of document 1 & quote & ">" & return
	set lastPage to count pages of document 1
	set firstPage to 1
	
	repeat with currentPage from firstPage to lastPage
		set pageNotes to notes of page currentPage of document 1
		
		set xml to xml & tab & "<page number=" & quote & currentPage & quote & ">" & return
		
		repeat with pageNote in pageNotes
			set xml to xml & tab & tab & "<note type=" & quote & (get type of pageNote) & quote & ">" & return
			set noteText to (get text of pageNote)
			set xml to xml & tab & tab & tab & "<text><![CDATA[" & noteText & "]]></text>" & return
			set noteBounds to get bounds for pageNote
			
			set pageHeight to get item 3 of (get bounds for (page currentPage of document 1))
			set x to item 1 of noteBounds
			set y to item 4 of noteBounds
			set width to (item 3 of noteBounds) - x
			set height to (item 2 of noteBounds) - y
			set y to pageHeight - y
			
			set xml to xml & tab & tab & tab & "<bounds " & "x=" & quote & x & quote & " y=" & quote & y & quote & " width=" & quote & width & quote & " height=" & quote & height & quote & "/>" & return
			
			set noteColor to get color of pageNote
			set cRed to item 1 of noteColor
			set cGreen to item 2 of noteColor
			set cBlue to item 3 of noteColor
			set cAlpha to item 4 of noteColor
			
			set xml to xml & tab & tab & tab & "<color " & "red=" & quote & cRed & quote & " green=" & quote & cGreen & quote & " blue=" & quote & cBlue & quote & " alpha=" & quote & cAlpha & quote & "/>" & return
			
			set xml to xml & tab & tab & "</note>" & return
		end repeat
		
		set xml to xml & tab & "</page>" & return
		
	end repeat
	
	set xml to xml & "</document>"
	return xml
	
end tell