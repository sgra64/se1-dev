# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Script to setup the project environment with environment variables:
#  - CLASSPATH, MODULEPATH      ; used by Java compiler and JVM
#  - JDK_JAVAC_OPTIONS          ; used by the Java compiler
#  - JDK_JAVADOC_OPTIONS        ; used by the Javadoc compiler
#  - JUNIT_OPTIONS              ; used by the JUnit test runner
#  - JACOCO_AGENT               ; JVM jacoco agent option for code coverage
#  - JAVAC_VERSION              ; .class version: 11|17|21 (externally set,
# \\                            ; requires re-sourcing to be effective)
# and created project files:
#  - .classpath, .project       ; files in project directory used by eclipse
#                               ; and the VSCode Java extension
# \\
# Script defines executable functions and aliases:
#  - cmd [cmd] [--single-line]  ; output full command for cmd argument in multi-
#                               ; or single-line format (with traling '\')
#  - show [-all] [--single-line] [cmd1, cmd2...] [args]  ; show command sequence
#  - build | make | mk [--show] [--silent|-s] [cmd1, cmd2...] [args]
#                                   ; execute cmd sequence, if not --show
#  - clean                      ; remove artefacts produced by commands
#  - wipe                       ; remove the entire project environment
#  - source .env/setenv.sh      ; build / re-build the project environment
# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

# set command aliases
[ -z "$(alias mk 2>/dev/null)" ] && aliases_present=false || aliases_present=true
alias mk="make"
alias build="make"
alias wipe="make wipe --silent"
alias clean="make clean"

# map with keys and project paths and files
declare -gA P=(
    [src]="src"
    [tests]="tests"
    [res]="resources"
    [lib]="libs"
    [target]="bin"
    [classes]="bin/classes"
    [test-classes]="bin/test-classes"
    [log]="logs"
    [doc]="docs"
    [cov]="coverage"
    [env]=".env"
    [url]="https://github.com/sgra64/se1.bestellsystem"
    [jar]="bin/application-1.0.0-SNAPSHOT.jar"
)

# list of short commands
cmd_shorts=("source" "project" "classpath" "cp" "compile" "compile-tests" "resources"
        "jar" "pack" "package" "pack-libs" "test-lib" "run" "run-jar" "run-tests"
        "coverage" "coverage-report" "javadoc" "clean" "wipe")


# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Main function to setup project environment (transient function).
# Usage: setup
function setup() {
    [ ! -d src ] && \
        echo "must source in project directory" && \
        return

    [ ! -f ${P[env]}/init.classpath ] && \
        echo "need to get all ${P[env]} files from repository: ${P[url]}" \
        return

    # if 'libs' directory is not in the project, attempt to link it from an
    # alternative path, e.g. where the 'libs'-branch might be checked out
    local lib_paths=(
        "./libs/libs" "../libs/libs" "../../libs/libs" "../../../libs/libs"
                "../../../../libs/libs"
        "branches/libs/libs" "../branches/libs/libs" "../../branches/libs/libs"
                "../../../branches/libs/libs" "../../../../branches/libs/libs"
        "../libs" "../../libs" "../../../libs" "../../../../libs"
    )
    for lpath in "${lib_paths[@]}"; do
        [ ! -d ${P[lib]} ] && local libs_linked=$(wire_link $lpath ${P[lib]})
    done
    # adjust P[lib] for symlink (javac does not understand links)
    [ -L libs ] && P[lib]=$(trace_link libs)

    # define local variables and fill with values
    local module_dirs=( $(find ${P[lib]}/* -type d 2>/dev/null) )
    local module_jars=( $(find ${P[lib]}/*/ -name '*.jar' 2>/dev/null) )
    local entries=(
        ${P[classes]} ${P[test-classes]} ${P[target]}/resources
        ${module_jars[@]}
    )

    # CLASSPATH seperator: Unix/Linux/Mac use ":", Windows uses ";" 
    [ "$(uname | grep 'CYGWIN\|MINGW')" ] && local sep=';' || local sep=':'

    local created=()    # collect environment variables set
    # build CLASSPATH environment variable, if not exists
    if [ -z "$CLASSPATH" ]; then
        export CLASSPATH=""
        for entry in ${entries[@]}; do
            [ ! -z "$CLASSPATH" ] && CLASSPATH+=${sep}
            CLASSPATH+=$entry
        done
        created+=("CLASSPATH")
    fi

    # build MODULEPATH environment variable, if not exists
    if [ -z "$MODULEPATH" ]; then
        export MODULEPATH=""
        for entry in ${module_dirs[@]}; do
            [ ! -z "$MODULEPATH" ] && MODULEPATH+=${sep}
            MODULEPATH+=$entry
        done
        created+=("MODULEPATH")
    fi

    local mp_opt=""
    [ "$MODULEPATH" ] && mp_opt=" --module-path \"$MODULEPATH\""

    # set JDK_JAVAC_OPTIONS used by the javac compiler
    if [ -z "$JDK_JAVAC_OPTIONS" ]; then
        # -Xlint:-module supresses warning: module name should not end with digit
        export JDK_JAVAC_OPTIONS="-Xlint:-module -d ${P[classes]}$mp_opt"
        created+=("JDK_JAVAC_OPTIONS")
    fi

    # set JDK_JAVADOC_OPTIONS used by the javadoc compiler
    if [ -z "$JDK_JAVADOC_OPTIONS" ]; then
        local jdoc_opts=(
            "--source-path ${P[src]} -d ${P[doc]}$mp_opt "
            "-version -author -noqualifier \"java.*:application.*\""
        )
        # append packages from src/main later in javadoc command: + "application"
        export JDK_JAVADOC_OPTIONS=${jdoc_opts[@]}
        created+=("JDK_JAVADOC_OPTIONS")
    fi

    # set JUNIT_OPTIONS used by JUnit test runner
    [ -z "$JUNIT_OPTIONS" ] && export JUNIT_OPTIONS=$( \
            [[ $(uname -s) =~ ^MINGW.* ]] && echo -n "--details-theme=ascii " || \
                echo -n "--details-theme=unicode "; \
            [[ "$COLOR" == "off" ]] && echo -n "--disable-ansi-colors "; \
            echo -n "-cp ${P[classes]} -cp ${P[test-classes]}"; \
        ) \
        && created+=("JUNIT_OPTIONS")

    # set JACOCO_AGENT for recording code coverage events during JUnit tests
    [ -z "$JACOCO_AGENT" ] && export JACOCO_AGENT=$( \
            agent=$(echo -n $CLASSPATH | tr "[;:]" "\n" | grep jacocoagent.jar); \
            echo -n "-javaagent:${agent}=output=file,destfile=${P[cov]}/jacoco.exec"; \
        ) \
        && created+=("JACOCO_AGENT")

    # report created environment variables
    [ "${created}" ] && echo "setting the project environment" && \
        echo " - environment variables:" && \
        for var in "${created[@]}"; do
            echo "    - $var"
        done

    # created=("$libs_linked")    # collect created files
    created=()
    [ "$libs_linked" ] && created+=("$libs_linked")
    # 
    # create .env/.classpath file for VSCode code-runner (settings.json)
    [ ! -f .env/.classpath ] \
        && echo "-cp $CLASSPATH" > .env/.classpath \
        && created+=(".env/.classpath")

    # create .classpath file used by VSCode/Java and eclipse IDE
    [ ! -f .classpath -a -f ${P[env]}/init.classpath ] \
        && eclipse_classpath "${module_jars[@]}" > .classpath \
        && created+=(".classpath")

    # create .project file used by VSCode/Java and eclipse
    [ ! -f .project -a -f ${P[env]}/init.project ] \
        && cp ${P[env]}/init.project .project \
        && created+=(".project")

    # report created files, if any
    [ "${created}" ] && echo " - files created:" && \
        for file in "${created[@]}"; do
            echo "    - $file"
        done

    # report created aliases and functions
    [ "$aliases_present" = "false" ] && echo " - functions and aliases created:" && \
        echo "    - aliases: mk, build, wipe, clean" && \
        echo "    - functions: make, show, cmd, copy, javac_version, coverage_report" && echo "//"

    echo "project environment is set (use 'wipe' to reset)"
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Output command for short argument multi- or single-line (no/with traling '\').
# Usage:
#   cmd [cmd] [--single-line]
# @param cmd command short, e.g. 'compile', 'compile-tests', 'run'...
# @param --single-line output in single-line format with trailing '\'
# @output full command for short cmd to stdout
function cmd() {
    local cmd=()
    case "$1" in
    source|project) cmd=("source ${P[env]}/setenv.sh") ;;
    classpath|cp)   cmd=("echo \$CLASSPATH | tr \"[;:]\" \"\\\n\"") ;;
    compile)    cmd=("javac $(javac_version javac)\$(find ${P[src]} -name '*.java') -d ${P[classes]}; \\"
                "$(cmd resources $2)"
                ) ;;
    compile-tests) cmd=("javac $(javac_version javac)\$(find ${P[tests]} -name '*.java') -d ${P[test-classes]}; \\"
                "$(cmd resources $2)"
                ) ;;
    resources)  cmd=("copy ${P[res]} ${P[target]}/resources")
                ;;
    jar|pack|package) cmd=("jar -c -v -f ${P[jar]} \\"
                "    -m ${P[res]}/META-INF/MANIFEST.MF \\"
                "    -C ${P[classes]} . ; \\"
                "jar uvf ${P[jar]} -C ${P[target]} resources;")
                ;;
    pack-libs)  cmd=("[ ! -d ${P[target]}/unpacked ] && mkdir -p ${P[target]}/unpacked && \\"
                "  echo inflating libs to ${P[target]}/unpacked && \\"
                "  (cd ${P[target]}/unpacked; find ../../libs/*/ -type f | xargs -I % jar xf %); \\"
                "jar uvf ${P[jar]} -C ${P[target]}/unpacked org")
                ;;
    test-lib)   cmd=("mk clean compile-tests; \\"
                "  javac_version manifest > ${P[target]}/manifest.mf; \\"
                "  jar -c -v -m ${P[target]}/manifest.mf -f ${P[target]}/test-lib.jar -C ${P[test-classes]} . ; \\"
                # "  jar uvf ${P[target]}/test-lib.jar -C ${P[target]} resources; \\"
                "  rm ${P[target]}/manifest.mf")
                ;;
    run)        cmd=("java application.Application") ;;
    run-jar)    cmd=("java -jar ${P[jar]}") ;;
    run-tests)  cmd=("java -jar ${P[lib]}/junit-platform-console-standalone-1.9.2.jar \\"
                "  \$(eval echo \$JUNIT_OPTIONS)"
                # "-c application.Application_0_always_pass_Tests \\"
                "--scan-class-path")
                ;;
    coverage)  cmd=("java \$(eval echo \$JACOCO_AGENT) \\"
                "  -jar ${P[lib]}/junit-platform-console-standalone-1.9.2.jar \\"
                "  \$(eval echo \$JUNIT_OPTIONS) --scan-class-path; \\"
                "  echo coverage events recorded in: ${P[cov]}/jacoco.exec")
                ;;
    coverage-report) cmd=("coverage_report; [ -f ${P[cov]}/index.html ] && \\"
                "  echo coverage report created in: ${P[cov]}/index.html")
                ;;
    javadoc)    # append package names containing .java files after $JDK_JAVADOC_OPTIONS
                cmd=("javadoc -d ${P[doc]} \$(eval echo \$JDK_JAVADOC_OPTIONS) \\"
                "  \$(cd src; find . -type d | sed -e 's/\.[\/]*//' -e 's/\//./g')")
                ;;
    clean)  cmd=("rm -rf ${P[target]} ${P[log]} ${P[doc]} ${P[cov]}")
            ;;
    wipe)   # reset project settings and clear the project directory of generated files
            local wipe_files=( .env/.classpath .classpath .project )
            cmd=("rm -rf ${wipe_files[@]} ; \\"
                 "$(cmd clean $2); \\"
                 "unset P cmd_shorts CLASSPATH MODULEPATH JDK_JAVAC_OPTIONS JACOCO_AGENT; \\"
                 "unset JDK_JAVADOC_OPTIONS JUNIT_OPTIONS JAVAC_VERSION aliases_present analyze_classes; \\"
                 "unset -f cmd show make copy javac_version coverage_report; \\"
                 "unalias mk build wipe clean; \\"
                 "[ -L libs ] && rm libs"
            ) ;;
    esac
    case "$2" in
    --single-line)
            shift; shift; [[ "${cmd[@]}" ]] && echo -en "${cmd[@]} $@" | sed -e 's/\\ /\\\n/g' ;;
    *)      shift; echo -en "${cmd[@]} $@" | sed -e 's/\\ /\n/g' ;;
    esac
    [[ "${cmd[@]}" ]] && echo
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Show full commands for sequence of command shorts (e.g. 'run', 'run-tests').
# Usage:
#   show [--all] [--single-line] [cmd1] [cmd2] [cmd3] [args] ...
# @param --all show all commands (reduced list without)
# @param --single-line output with trailing '\'
# @param cmd command short, e.g. 'compile', 'compile-tests', 'run'...
# @param args arguments passed to last cmd
# @output command list to stdout
function show() {
    local show_all=false
    for arg in "$@"; do
        [[ "$arg" = "--all" ]] && show_all=true && shift && break
    done
    if [[ -z "$@" ]]; then
        local h1=""; local h2="";
        [[ "$COLOR" = "on" ]] && h1="\e[1;37m" && h2="\e[m"
        for short in ${cmd_shorts[@]}; do
            # skip short commands not shown without '--all' option
            [[ "$show_all" = "false" ]] && case "$short" in \
                coverage|coverage-report|package|pack-libs|test-lib|run-jar) continue;
                esac
            # list with highlighted short and indented full command
            case "$short" in
            project|jar|pack|classpath) ;;    # skip command aliases (ignore)
            wipe)    echo -e "${h1}wipe:${h2}"; echo ;;
            source)  echo -e "${h1}source | project:${h2}"; echo "  $(cmd $short)"; echo ;;
            cp)      echo -e "${h1}classpath | cp:${h2}";   echo "  $(cmd $short)"; echo ;;
            package) echo -e "${h1}jar | pack | package:${h2}"; cmd $short --single-line | sed -e 's/^/  /'; echo ;;
            *)       echo -e "${h1}$short:${h2}";               cmd $short --single-line | sed -e 's/^/  /'; echo ;;
            esac
        done
    else
        # show sequence of command shorts passed as args
        make --show $@
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Execute sequence of command shorts (e.g. 'compile', 'run', 'run-tests'...).
# Usage:
#   make [--show] [-s|--silent] [cmd1] [cmd2] [cmd3] [args] ...
# @param --show show full command, but do not execute
# @param -s|--silent execute command, but don't show (default)
# @param cmd command short, e.g. 'compile', 'compile-tests', 'run', ...
# @param args arguments passed to last cmd
function make() {
    local exec=true
    local silent=false
    local singleline=""
    local arg_cmd_shorts=() # short commands passed as arguments
    local args=()           # other args (none-cmd_shorts)
    for arg in "$@"; do
        local len=${#arg_cmd_shorts[@]}
        for cmd in ${cmd_shorts[@]}; do     # match arg in cmd_shorts[@]
            [[ "$cmd" == "$arg" ]] && arg_cmd_shorts+=($arg) && break
        done
        if [[ ${#arg_cmd_shorts[@]} == $len ]]; then
            case "$arg" in
            --show) exec=false ;;
            -s|--silent) silent=true ;;
            --single-line) singleline="--single-line" ;;
            *) args+=($arg) ;;      # collect other args as last-cmd args
            esac
        fi
    done
    if [[ ! -z "${arg_cmd_shorts[@]}" ]]; then
        for short in "${arg_cmd_shorts[@]}"; do
            [ ! -d src -a "$exec" = "true" ] && \
                echo -e "must execute \047$short\047 in project directory" && \
                return
            # 
            local exec_cmd=""
            local pass_args=""      # pass args to last cmd
            [[ "${arg_cmd_shorts[-1]}" = "$short" ]] && pass_args="${args[@]}"
            # 
            [[ "$silent" = "false" ]] && cmd $short "--single-line" $pass_args
            [[ "$exec" = "true" ]] && eval $(cmd $short $pass_args) && echo "done."
        done
    else
        show --all
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Attempt to link a directory from a destination (e.g. libs -> ../libs/libs).
# GitBash understands links, but does not create links (copies files instead).
# Usage:
#   wire_link [src_path] [link_name]
# @param src_path origination path (as directory, not link itself)
# @param link_name name of created link
function wire_link() {
    local src_path=$(trace_link "$1")
    local link_name="$2"
    [ ! -d $link_name -a ! -L $link_name -a -d $src_path ] && \
        ln -s $src_path $link_name && \
            [ -L $link_name ] && echo " ln -s $(readlink $link_name) $link_name"
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Trace link sequence (links pointing to links) to final file or directory.
# Usage:
#   trace_link [link]
# @param link starting point of tracing
# @output path to final directory or link
function trace_link() {
    local link=$1
    local trace=""
    while [ -L "$link" ]; do
        link=$(readlink $link)
        if [ -z "$trace" ]; then
            trace=$link
        else
            trace=$(dirname $trace)
            trace+="/"$link
        fi
    done
    echo $([ -z "$trace" ] && echo $link || echo $trace)
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Copy content of (resource) directory passed as first argument to directory
# passed as second argumet, except 'META-INF' directory.
# Usage:
#   copy [from_dir] [to_dir]
# @param from_dir source directory from which content is recursively copied
# @param to_dir destination directory
function copy() {
    local from_dir="$1"
    local to_dir="$2"
    if [ -z "$from_dir" ] || [ -z "$to_dir" ]; then
        echo "use: copy <from-dir> <to-dir>"
    else
        [[ ! -d "$to_dir" ]] && mkdir -p $to_dir
        # find $from_dir ! -path '*META-INF*' -type f | xargs cp --parent -t $to_dir
        tar -cpf - -C $from_dir --exclude='META-INF' . | tar xpf - -C $to_dir
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Output eclipse and VSCode(Java) IDE configurations to generate .classpath
# file in the project directory, needed by IDE to understand the project
# structure (transient function).
# Usage:
#   eclipse_classpath [module_jars[@]]
# @param module_jars[@] module_jars array
# @output content of configured .classfile to stdout
function eclipse_classpath() {
    local jars=("$@")
    if [ "${#jars[@]}" -gt "0" ]; then
        # remove closing tag </classpath>
        egrep -v '</classpath>' < ${P[env]}/init.classpath
        # insert <classpathentry> ... </classpathentry>
        for jar in ${jars[@]}; do
            echo '    <classpathentry kind="lib" path="'${jar}'">'
            echo '        <attributes>'
            echo '            <attribute name="module" value="true"/>'
            echo '        </attributes>'
            echo '    </classpathentry>'
        done
        # re-append closing tag </classpath>
        echo '</classpath>'
    else
        cat ${P[env]}/init.classpath
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Output javac compile version option and jar-MANIFEST version information.
# Test compiled version of .class and .jar file:
# - javap -verbose file.class | grep major
# - jar tvf file.jar -> META-INF/MANIFEST.MF -> Created-By: 17 (Oracle Corporation)
# 
# Java version codes:
# Java  8(52), Java  9(53), Java 10(54) - do not compile codebase
# Java 11(55), Java 12(56)
# Java 13(57), Java 14(58), Java 15(59), Java 16(60)
# Java 17(61), Java 18(62), Java 19(63), Java 20(64)
# Java 21(65), Java 22(66), Java 23(67), Java 24(68)
# 
# Usage:
#   javac_version [javac, manifest]
# @param output selector
# @output javac compile version information or empty for current version
function javac_version() {
    case "$JAVAC_VERSION" in
    11|17|21)
        [ "$1" = "javac" ] && echo "-source $JAVAC_VERSION -target $JAVAC_VERSION "
        [ "$1" = "manifest" ] && echo "Created-By: $JAVAC_VERSION (Oracle Corporation)"
        ;;
    esac
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Generate code-coverage report based on events recorded in file:
#   - ${P[cov]}/jacoco.exec
# 
# Code coverage report needs definition of source classes to analyse in form:
#   - analyze_classes=(
#       --classfiles ./bin/classes/datamodel/Article.class
#       --classfiles ./bin/classes/datamodel/Customer.class
#       --classfiles ./bin/classes/datamodel/Currency.class
#       --classfiles ./bin/classes/datamodel/Order.class
#       --classfiles ./bin/classes/datamodel/OrderItem.class
#       --classfiles ./bin/classes/datamodel/TAX.class
#     )
# Usage:
#   coverage_report [html, csv, xml]
# @param format output format (html is default)
# @output error message or empty
function coverage_report() {
    if [ ! -f "${P[cov]}/jacoco.exec" ]; then
        echo "--> run coverage agent to create: ${P[cov]}/jacoco.exec"
    else
        if [ -z "${analyze_classes}" ]; then
            echo "--> define variable: \$analyze_classes, e.g. with:"
            echo "  analyze_classes=("
            find ${P[classes]} | grep 'datamodel/[A-Z]' | sed -e 's/^/    --classfiles .\//'
            echo "  )"
        else
            local output="--html ${P[cov]}"
            case "$1" in
            csv|.csv) output="--csv ${P[cov]}/coverage.csv" ;;
            xml|.xml) output="--xml ${P[cov]}/coverage.xml" ;;
            esac
            local reporter=$(echo -n $CLASSPATH | tr "[;:]" "\n" | grep jacococli.jar);
            # zsh need eval() funtion to execute java
            $(eval echo java -jar $reporter report ${P[cov]}/jacoco.exec \
                --sourcefiles ${P[src]} $output ${analyze_classes[@]})
        fi
    fi
}


# execute setup function
setup

# remove transient varibales and functions from shell-process
unset -f setup wire_link trace_link eclipse_classpath
