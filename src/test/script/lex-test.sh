#!/bin/sh


cd "$(dirname "$0")"/../../.. || exit

ARG1=${1:-foo}


red=`tput setaf 1`
green=`tput setaf 2`
reset=`tput sgr0`
yellow=$(tput setaf 3)
background_cyan=$(tput setab 7)

failed=0
all=0

echo "$yellow\t------- VALID TESTS ----------$reset\n"
for i in $(find ./src/test/deca/syntax/valid/ -name '*.deca')


do
    all=$((all+1))

    echo "$i:"
    if test_lex $i 2>&1 | grep -q -e 'syntaxError'
    
    then
        failed=$((failed+1))
        echo "${red}Echec inattendu pour test_lex$reset"
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
    else
        echo "${green}Succes attendu de test_lex$reset"
        
    fi
    
done

for i in $(find ./src/test/deca/context/ -name '*.deca')

do
    all=$((all+1))
    echo "$i:"
    if test_lex $i 2>&1 | grep -q -e 'syntaxError'
    
    then
        echo "${red}Echec inattendu pour test_lex$reset"
        failed=$((failed+1))
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
    else
        echo "${green}Succes attendu de test_lex$reset"
        
    fi
    
done

echo "\n$yellow\t------- INVALID TESTS ----------$reset\n"
for i in $(find ./src/test/deca/syntax/invalid/created/lexique -name '*.deca')

do
    all=$((all+1))

    echo "$i:"
    if test_lex $i 2>&1 | grep -q -e 'syntaxError'
    
    then
        echo "${green}Echec attendu pour test_lex$reset"
    else
        failed=$((failed+1))
        echo "${red}Succes inattendu de test_lex$reset"
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
        
    fi
    
done



echo "\n\t ------------- $failed tests failed out of $all tests ------------"