#!/bin/sh


cd "$(dirname "$0")"/../../.. || exit 1

ARG1=${1:-foo}

red=`tput setaf 1`
green=`tput setaf 2`
reset=`tput sgr0`
yellow=$(tput setaf 3)
background_cyan=$(tput setab 7)

failed=0
all=0

echo "$yellow\t------- VALID TESTS ----------$reset\n"
for i in $(find ./src/test/deca/context/valid/ -name '*.deca')

do
    all=$((all+1))
    echo "$i:"
    if test_context $i 2>&1 | grep -q -e "$i" -e "ERREUR" -e "ERROR" -e "introuvable" -e "EXCEPTION" -e "introuvable"
    
    then
        echo "${red}Echec inattendu pour test_context$reset"
        failed=$((failed+1))
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
    else
        echo "${green}Succes attendu de test_context$reset"
        
    fi
    
done

echo "\n$yellow\t------- INVALID TESTS ----------$reset\n"


for i in $(find ./src/test/deca/context/invalid/ -name '*.deca')
do
    all=$((all+1))    
    echo "$i:"
    nom_fichier="${i%.*}"
    #if test_context $i 2>&1 | grep -q -e "$i"
    test_context "$nom_fichier.deca" > "$nom_fichier.res"  2>&1
    if grep -Ff "$nom_fichier.des" "$nom_fichier.res" >/dev/null  ;
    then
        echo "${green}Echec attendu pour test_context$reset"
    else
        echo "${red}Succes inattendu de test_context$reset"
        failed=$((failed+1))
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
        
    fi
    find ./src/test/deca/context/invalid/ -name '*.res' -type f -delete
done

echo "\n\t ------------- $failed tests failed out of $all tests ------------"