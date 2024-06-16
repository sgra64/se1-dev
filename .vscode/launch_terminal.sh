#------------------------------------------------------------------------------
# launch script for terminal (bash, zsh for Mac)
#------------------------------------------------------------------------------
# 
# probe for java command being set on PATH
if [[ $(type java &>/dev/null; echo $?) == 0 ]]; then
    echo "source ~/.bashrc (or ~/.zshrc for Mac)"
    [[ "$SHELL" =~ ^.*zsh$ ]] && \
        source ${HOME}/.zshrc || source ${HOME}/.bashrc
    # 
    # switch Windows default code page to UTF-8 (only for Windows)
    [[ $(type chcp.com &>/dev/null; echo $?) == 0 ]] && \
        chcp.com 65001 >/dev/null
fi

# source project, if unsoured (setup function in .env/project.sh exists)
[[ -z "$(type setup 2>/dev/null | grep function)" ]] && \
    echo "source .env/project.sh" && \
    source .env/project.sh

cd .
