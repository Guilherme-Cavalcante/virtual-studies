package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.message.MessageRequestDTO;
import br.ifsp.virtual_studies.dto.message.MessageResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.MessageRepository;
import br.ifsp.virtual_studies.repository.UsuarioRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UsuarioRepository usuarioRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public MessageResponseDTO createMessage(MessageRequestDTO messageDto) {
        Chat chat = chatRepository.findById(messageDto.getChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        Usuario author = usuarioRepository.findById(messageDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Message message = new Message();
        message.setChat(chat);
        message.setAuthor(author);
        message.setText(messageDto.getText());
        message.setCreatedAt(LocalDateTime.now());
        Message createdMessage = messageRepository.save(message);
        return modelMapper.map(createdMessage, MessageResponseDTO.class);
    }
    
    public PagedResponse<MessageResponseDTO> getAllMessages(Pageable pageable) {
        Page<Message> messagesPage = messageRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(messagesPage, MessageResponseDTO.class);
    }
    
    public MessageResponseDTO getMessageById(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        return modelMapper.map(message, MessageResponseDTO.class);
    }
    
    public MessageResponseDTO updateMessage(Long id, MessageRequestDTO messageDto) {
        
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with ID: " + id));
        
        modelMapper.map(messageDto, existingMessage);
        existingMessage.setId(id);
        Message updatedMessage = messageRepository.save(existingMessage);
        return modelMapper.map(updatedMessage, MessageResponseDTO.class);
    }
    
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with ID: " + id));
        
        messageRepository.delete(message);
    }
}