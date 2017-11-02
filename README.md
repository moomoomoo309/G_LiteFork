# G_Lite

### A forked version of the G_Lite tool used in NJIT's CS252 course for compiling and assembling C and ARM assembly code.

## Command line arguments
- --runexecutable=(executablename)
  - Specifies the name/path of the binary used for running. Defaults to "arm-none-eabi-run".
- --compileexecutable=(compileexecutable)
  - Specifies the name/path of the binary used for compiling. Defaults to "arm-none-eabi-gcc".

## How to use
- Write your C and ARM Assembly code
  - Use a nice text editor for assembly. (Don't use notepad!)
    - Recommended Text Editors: Sublime Text (with ARM Assembly plugin), Vim, Emacs, Notepad++, Notepad 2
- Pick your C file and press Compile.
- Pick your Assembly file and press Assemble.
- Press Link
- Press Run
- Either
  - Press Print to print out the output
- or
  - Press File to get your output as a txt file.
- (Optional) Press clean to remove the leftover files.
- Done!
