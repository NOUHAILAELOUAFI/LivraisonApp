package com.example.commandeservice.controller;

import com.example.commandeservice.entite.Commande;
import com.example.commandeservice.modele.*;
import com.example.commandeservice.repository.CommandeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

@RestController

public class CommandeController {
    private CommandeRepository commandeRepository;
    private ProduitOpenFeign produitOpenFeign;
    private ClientOpenFeign clientOpenFeign;
    private LivreurOpenFeign livreurOpenFeign;

    public CommandeController(CommandeRepository cmd,ProduitOpenFeign pd,ClientOpenFeign cl,LivreurOpenFeign l){
        this.commandeRepository=cmd;
        this.produitOpenFeign=pd;
        this.clientOpenFeign=cl;
        this.livreurOpenFeign=l;
    }

    private List<Commande> getCommandes(List<Commande> cmd) {
        for(Commande c :cmd){
            for(commandeItem item : c.getItems()){
                Produit p = produitOpenFeign.findById(item.getIdProduit());
                p.setQuantity(item.getQuantity());
                c.getProducts().add(p);
            }
            Client cl = clientOpenFeign.findById(c.getIdClient());
            c.setClient(cl);
            Livreur l1 = livreurOpenFeign.findById(c.getIdLivreur());
            c.setLivreur(l1);
        }
        return cmd;
    }

    @GetMapping("/commandes")
    public List<Commande> getallcmd(){
        List<Commande> cmd= commandeRepository.findAll();
        return getCommandes(cmd);
    }
    @GetMapping("/commandes/{idCmd}")
    public Commande getCommande(@PathVariable String idCmd){
        return commandeRepository.findById(idCmd).get();
    }

    @GetMapping("/commandes/livr/{id}")
    public List<Commande> getcmdbyLivr(@PathVariable(name = "id") String id){
        Predicate<Commande> ps = cmd -> cmd.getIdLivreur().equals(id);
        List<Commande> cmd = commandeRepository.findAll().stream().filter(ps).toList();

        return getCommandes(cmd);
    }
    @GetMapping("/commandes/clt/{id}")
    public List<Commande> getcmdbyClt(@PathVariable(name = "id") String id){
        Predicate<Commande> ps = cmd -> cmd.getIdClient().equals(id);
        List<Commande> cmd = commandeRepository.findAll().stream().filter(ps).toList();
        return getCommandes(cmd);
    }

    @PostMapping("/commandes")
    public void createCommande(@RequestBody Commande c){
        commandeRepository.save(c);
    }

    @DeleteMapping("/commandes/{id}")
    public void deleteCommande(@PathVariable(name = "id") String code){
        commandeRepository.deleteById(code);
    }
    @PutMapping("/commandes/{id}")
    public void updateCommande(@PathVariable(name = "id") String code,@RequestBody Commande c){
        Commande cmd1= commandeRepository.findById(code).get();
        cmd1.setIdClient(c.getIdClient());
        cmd1.setIdLivreur(c.getIdLivreur());
        cmd1.setLivreur(c.getLivreur());
        cmd1.setClient(c.getClient());
        cmd1.setItems(c.getItems());
        cmd1.setProducts(c.getProducts());
        cmd1.setStatus(c.getStatus());
        commandeRepository.save(cmd1);
    }
}
