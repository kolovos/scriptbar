tell application "Skim"
	
	set docName to name of document 1
	set docText to get text of document 1 as string
	set selText to get selection of document 1 as string
	
	
	
	
	set docWords to count of words of docText
	set docChars to count of characters of docText
	if length of docText is 0 then
		set docParas to 0
	else
		set docParas to count of paragraphs of docText
	end if
	
	set docTextInfo to "Lines in document: " & docParas & return & Â
		"Words in document: " & docWords & return & Â
		"Characters in document: " & docChars
	set docInfoMsg to docTextInfo
	
	set selWords to count of words of selText
	set selChars to count of characters of selText
	if length of selText is 0 then
		set selParas to 0
		set selTextInfo to ""
	else
		set selParas to count of paragraphs of selText
		if length of paragraph -1 of selText is 0 then
			-- If the person highlighted two lines, including the line return 
			-- at the end of the second line, 'paragraphs' think there is a third line,
			-- since it splits into paragraphs using the number returns. If so, 
			-- subtract 1 from the count to avoid confusing people - they see only 
			-- two lines highlighted, so script should agreee with that. 
			set selParas to selParas - 1
		end if
		set selTextInfo to "Lines in selection: " & selParas & return & Â
			"Words in selection: " & selWords & return & Â
			"Characters in selection: " & selChars
		set docInfoMsg to docInfoMsg & return & return & selTextInfo
	end if
	
	
	--		display dialog "" default answer docTextInfo & return & return & selTextInfo buttons {"OK"} default button "OK" with title "Document Info: " & docName
	set infoAlertReply to (display alert "Document Info: " & docName message docInfoMsg buttons {"More Info", "Copy Info", "OK"} default button "OK")
	
	if button returned of infoAlertReply is "Copy Info" then
		set the clipboard to docInfoMsg
	else if button returned of infoAlertReply is "More Info" then
		set docInfo to info of document 1
		
		set docInfoMsg to docInfoMsg & return
		set docInfoMsg to docInfoMsg & "-----------------------------------" & return
		set docInfoMsg to docInfoMsg & "file name: " & file name of docInfo & return
		set docInfoMsg to docInfoMsg & "title: " & title of docInfo & return
		set docInfoMsg to docInfoMsg & "page count: " & page count of docInfo & return
		set docInfoMsg to docInfoMsg & "current page: " & label of current page of document 1 & return
		set docInfoMsg to docInfoMsg & "author: " & author of docInfo & return
		set docInfoMsg to docInfoMsg & "creation date: " & creation date of docInfo & return
		set docInfoMsg to docInfoMsg & "modification date: " & modification date of docInfo & return
		set docInfoMsg to docInfoMsg & "file size: " & file size of docInfo & return
		set docInfoMsg to docInfoMsg & "physical size: " & physical size of docInfo & return
		set docInfoMsg to docInfoMsg & "logical size: " & logical size of docInfo & return
		set docInfoMsg to docInfoMsg & "page size: " & page size of docInfo & return
		
		set docInfoMsg to docInfoMsg & "producer: " & producer of docInfo & return
		set docInfoMsg to docInfoMsg & "version: " & version of docInfo & return
		set docInfoMsg to docInfoMsg & "creator: " & creator of docInfo & return
		set docInfoMsg to docInfoMsg & "encrypted: " & encrypted of docInfo & return
		set docInfoMsg to docInfoMsg & "allows copying: " & allows copying of docInfo & return
		set docInfoMsg to docInfoMsg & "allows printing: " & allows printing of docInfo & return
		set docInfoMsg to docInfoMsg & "file path: " & path of document 1 & return
		
		set docInfoMsg to docInfoMsg & "-----------------------------------" & return
		set docViewSettings to view settings of document 1
		set docInfoMsg to docInfoMsg & tab & tab & "View Settings" & return
		
		-- display mode uses application constants that get turned into bracketed descriptions when script is run from Script Menu
		if single page is display mode of docViewSettings then
			set displayModeText to "single page"
		else if single page continuous is display mode of docViewSettings then
			set displayModeText to "single page continuous"
		else if two up is display mode of docViewSettings then
			set displayModeText to "two up"
		else if two up continuous is display mode of docViewSettings then
			set displayModeText to "two up continuous"
		else
			set displayModeText to (display mode of docViewSettings) as string
		end if
		
		set docInfoMsg to docInfoMsg & "display mode: " & displayModeText & return
		set docInfoMsg to docInfoMsg & "scale factor: " & scale factor of docViewSettings & return
		set docInfoMsg to docInfoMsg & "displays page breaks: " & displays page breaks of docViewSettings & return
		set docInfoMsg to docInfoMsg & "auto scales: " & auto scales of docViewSettings & return
		
		-- display box uses application constants that get turned into bracketed descriptions when script is run from Script Menu
		if media box is display box of docViewSettings then
			set displayBoxText to "media box"
		else if crop box is display box of docViewSettings then
			set displayBoxText to "crop box"
		else
			set displayBoxText to (display box of docViewSettings) as string
		end if
		
		set docInfoMsg to docInfoMsg & "display box: " & displayBoxText & return
		set docInfoMsg to docInfoMsg & "displays as book: " & displays as book of docViewSettings & return
		
		set infoAlertReply to (display alert "Document Info: " & docName message docInfoMsg buttons {"Select Info", "Copy Info", "OK"} default button "OK")
		
		if button returned of infoAlertReply is "Copy Info" then
			set the clipboard to docInfoMsg
		else if button returned of infoAlertReply is "Select Info" then
			display dialog "" default answer docInfoMsg with title "Document Info: " & docName
			
		end if
		
	end if
	
end tell