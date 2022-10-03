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
for i in $(find ./src/test/deca/codegen/valid/created/ -name '*.deca')

do
    all=$((all+1))
    echo "$i:"
    nom_fichier="${i%.*}"
    if decac $i 2>&1 | grep -i -q -e "ERREUR" -e "ERROR" -e "introuvable"
        then
        echo "${red}Echec inattendu au niveau de la compilation pour test_codegen$reset"
        
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
    fi
    ima "$nom_fichier.ass" > "$nom_fichier.res"  2>&1 
    #if ! cmp -s "$nom_fichier.res" "$nom_fichier.des" >/dev/null ;
    if ! grep -Ff "$nom_fichier.des" "$nom_fichier.res" >/dev/null  ;
    then
        echo "${red}Echec inattendu au niveau de pour test_codegen$reset"
        failed=$((failed+1))
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
    else
        echo "${green}Succes attendu de test_codegen$reset"
        
    fi
    
done


echo "\n$yellow\t------- INVALID TESTS ----------$reset\n"
for i in $(find ./src/test/deca/codegen/invalid/created/ -name '*.deca')
do
    all=$((all+1))    
    echo "$i:"
    nom_fichier="${i%.*}"
    if decac $i 2>&1 | grep -i -q -e "ERREUR" -e "ERROR" 
        then
        echo "${red}Echec inattendu au niveau de la compilation pour test_codegen$reset"
        
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
    fi

    if ima "$nom_fichier.ass" 2>&1 | grep -i -q -e "ERREUR" -e "ERROR" 
    
    then
        echo "${green}Echec attendu pour test_codegen$reset"
    else
        echo "${red}Succes inattendu de test_codegen$reset"
        failed=$((failed+1))
        if [ "$ARG1" != "-n" ]; then
            exit 1
        fi
        
    fi
    find ./src/test/deca/codegen/ -name '*.res' -type f -delete
done

echo "\n\t ------------- $failed tests failed out of $all tests ------------"